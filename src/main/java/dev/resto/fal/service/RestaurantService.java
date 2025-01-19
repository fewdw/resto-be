package dev.resto.fal.service;

import dev.resto.fal.DTO.*;
import dev.resto.fal.client.RestaurantApiClient;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.exceptions.RestaurantAddLimitException;
import dev.resto.fal.exceptions.RestaurantAlreadyExistsException;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.entity.User;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.specification.RestaurantSpecification;
import dev.resto.fal.util.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Value("${restaurant.add-limit}")
    private int RESTAURANT_ADD_LIMIT;

    @Value("${restaurant.pages-limit}")
    private int RESTAURANTS_PER_PAGE;

    @Value("${tags.per.thumbnail}")
    private int TAGS_PER_THUMBNAIL;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantApiClient restaurantApiClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BucketService bucketService;


    public List<AddRestaurantThumbnail> searchRestaurants(String query) throws IOException {

        List<String> placeIds = restaurantApiClient.searchRestaurants(query);

        placeIds.removeIf(placeId -> restaurantRepository.existsByPlaceId(placeId));

        List<AddRestaurantThumbnail> addRestaurantThumbnailList = restaurantApiClient.getRestaurantThumbnails(placeIds);

        for (AddRestaurantThumbnail addRestaurantThumbnail : addRestaurantThumbnailList) {
            addRestaurantThumbnail.setImageUrl(bucketService.putObjectIntoBucket("search/", addRestaurantThumbnail.getImageUrl(), addRestaurantThumbnail.getPlaceId()));
        }

        return addRestaurantThumbnailList;

    }

    public RestaurantApiInfo addRestaurant(String placeId, String userId) throws IOException {

        if (restaurantRepository.existsByPlaceId(placeId)) {
            throw new RestaurantAlreadyExistsException("Restaurant already exists");
        }

        RestaurantApiInfo restaurantApiInfo = new RestaurantApiInfo();
        try {
            restaurantApiInfo = restaurantApiClient.getRestaurantInfo(placeId);
        } catch (Exception e) {
            throw new NotFoundException("Restaurant not found");
        }


        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        if (user.getNumberOfRestaurantsAdded() >= RESTAURANT_ADD_LIMIT) {
            throw new RestaurantAddLimitException("Restaurant add limit reached");
        }

        String s3ImageUrl = bucketService.putObjectIntoBucket("thumbnails/", restaurantApiInfo.getImageUrl(), restaurantApiInfo.getPlaceId());

        String restaurantUsername = UsernameGenerator.generateRandomUsername();
        while (restaurantRepository.existsByUsername(restaurantUsername)) {
            restaurantUsername = UsernameGenerator.generateRandomUsername();
        }

        restaurantApiInfo.setRestaurantUsername(restaurantUsername);

        Restaurant newRestaurant = new Restaurant(
                restaurantApiInfo.getPlaceId(),
                restaurantApiInfo.getRestaurantName(),
                restaurantApiInfo.getRestaurantAddress(),
                restaurantApiInfo.getGoogleMapUrl(),
                restaurantApiInfo.getWebsite(),
                restaurantApiInfo.getPhoneNumber(),
                s3ImageUrl,
                restaurantApiInfo.getWeekdayText(),
                user,
                LocalDateTime.now(),
                restaurantUsername
        );

        restaurantApiInfo.setImageUrl(s3ImageUrl);

        user.setNumberOfRestaurantsAdded(user.getNumberOfRestaurantsAdded() + 1);
        userRepository.save(user);

        restaurantRepository.save(newRestaurant);
        return restaurantApiInfo;
    }

    public RestaurantInfoPage getRestaurant(String restaurantUsername) {

        Restaurant restaurant = restaurantRepository.findByUsername(restaurantUsername).orElseThrow(
                () -> new NotFoundException("Restaurant not found")
        );

        UserInfoAddedBy userInfoAddedBy = new UserInfoAddedBy(
                restaurant.getUser().getName(),
                restaurant.getUser().getUsername(),
                restaurant.getUser().getPicture()
        );

        RestaurantApiInfo restaurantApiInfo = new RestaurantApiInfo(
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPlaceId(),
                restaurant.getLink(),
                restaurant.getWebsite(),
                restaurant.getPhoneNumber(),
                restaurant.getPhotoUrl(),
                restaurant.getWeekdayText(),
                restaurant.getUsername());

        return new RestaurantInfoPage(restaurantApiInfo, userInfoAddedBy);
    }

    public Boolean restaurantExistsByUsername(String restaurantUsername) {
        if (!restaurantRepository.existsByUsername(restaurantUsername)) {
            throw new NotFoundException("Restaurant not found");
        }
        return true;
    }

    public List<RestaurantThumbnail> getFilteredThumbnails(String userId, FilterRequest filterRequest, int page) {
        Specification<Restaurant> spec = Specification
                .where(RestaurantSpecification.containsSearchBar(filterRequest.getSearchBar()))
                .and(RestaurantSpecification.hasTags(filterRequest.getTags(), filterRequest.isStrictTags()));

        Sort sort = RestaurantSpecification.sortBy(filterRequest);

        Page<Restaurant> restaurantPage = restaurantRepository.findAll(spec, PageRequest.of(page, RESTAURANTS_PER_PAGE, sort));

        return restaurantPage.stream()
                .map(restaurant -> convertToThumbnail(restaurant, userId))
                .collect(Collectors.toList());
    }

    private RestaurantThumbnail convertToThumbnail(Restaurant restaurant, String userId) {
        RestaurantThumbnail thumbnail = new RestaurantThumbnail();
        thumbnail.setRestaurantImage(restaurant.getPhotoUrl());
        thumbnail.setRestaurantName(restaurant.getName());
        thumbnail.setRestaurantUsername(restaurant.getUsername());
        thumbnail.setRestaurantAddress(restaurant.getAddress());

        thumbnail.setLikedByUser(isRestaurantLikedByUser(restaurant, userId));

        List<RestaurantThumbnailRating> topRatings = restaurant.getAllTagsFromRatings().stream()
                .map(rating -> {
                    RestaurantThumbnailRating thumbRating = new RestaurantThumbnailRating();
                    thumbRating.setTag(rating.getTag());
                    thumbRating.setVotes(rating.getVotes());
                    return thumbRating;
                })
                .sorted((r1, r2) -> r2.compareTo(r1))
                .limit(TAGS_PER_THUMBNAIL)
                .collect(Collectors.toList());

        thumbnail.setRatings(topRatings);
        return thumbnail;
    }

    private boolean isRestaurantLikedByUser(Restaurant restaurant, String userId) {
        return userRepository.findById(userId).get().getFavorites().contains(restaurant);
    }

}
