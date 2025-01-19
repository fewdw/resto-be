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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Value("${restaurant.add-limit}")
    private int restaurantAddLimit;

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

        if(restaurantRepository.existsByPlaceId(placeId)){
            throw new RestaurantAlreadyExistsException("Restaurant already exists");
        }

        RestaurantApiInfo restaurantApiInfo = new RestaurantApiInfo();
        try{
             restaurantApiInfo = restaurantApiClient.getRestaurantInfo(placeId);
        } catch (Exception e){
            throw new NotFoundException("Restaurant not found");
        }


        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        if (user.getNumberOfRestaurantsAdded() >= restaurantAddLimit) {
            throw new RestaurantAddLimitException("Restaurant add limit reached");
        }

        String s3ImageUrl = bucketService.putObjectIntoBucket("thumbnails/", restaurantApiInfo.getImageUrl(), restaurantApiInfo.getPlaceId());

        String restaurantUsername = UsernameGenerator.generateRandomUsername();
        while(restaurantRepository.existsByUsername(restaurantUsername)){
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

    public RestaurantApiInfo getRestaurant(String restaurantUsername, String id) {

        Restaurant restaurant = restaurantRepository.findByUsername(restaurantUsername).orElseThrow(
                () -> new NotFoundException("Restaurant not found")
        );

        return new RestaurantApiInfo(
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPlaceId(),
                restaurant.getLink(),
                restaurant.getWebsite(),
                restaurant.getPhoneNumber(),
                restaurant.getPhotoUrl(),
                restaurant.getWeekdayText(),
                restaurant.getUsername()
        );
    }

    public List<RestaurantThumbnailOld> getFilteredThumbnails(String userId, FilterRequest filterRequest) {
        Specification<Restaurant> spec = Specification
                .where(RestaurantSpecification.containsSearchBar(filterRequest.getSearchBar()))
                .and(RestaurantSpecification.hasTags(filterRequest.getTags(), filterRequest.isStrictTags()))
                .and(RestaurantSpecification.sortBy(filterRequest));

        List<Restaurant> restaurants = restaurantRepository.findAll(spec);

        restaurants.forEach(restaurant -> restaurant.getRatings().sort(Comparator.comparingInt(rating -> -rating.getVotes())));

        return restaurants.stream()
                .map(restaurant -> new RestaurantThumbnailOld(
                        restaurant.getPhotoUrl(),
                        restaurant.getName(),
                        restaurant.getPlaceId(),
                        restaurant.getAddress(),
                        userRepository.findById(userId).get().getFavorites().contains(restaurant),
                        restaurant.getAllTagsFromRatings(),
                        restaurant.getUsername()
                ))
                .collect(Collectors.toList());
    }

    public Boolean restaurantExistsByUsername(String restaurantUsername) {
        if(!restaurantRepository.existsByUsername(restaurantUsername)){
            throw new NotFoundException("Restaurant not found");
        }
        return true;
    }
}
