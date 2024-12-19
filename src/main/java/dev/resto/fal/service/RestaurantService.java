package dev.resto.fal.service;

import dev.resto.fal.client.RestaurantApiClient;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.RestaurantApiInfo;
import dev.resto.fal.entity.User;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.response.RestaurantSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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


    public List<RestaurantSearch> searchRestaurants(String query, String userId) {
        return restaurantApiClient.searchRestaurants(query);
    }

    public RestaurantApiInfo addRestaurant(String placeId, String userId) throws IOException {

        if(restaurantRepository.existsByPlaceId(placeId)){
            throw new RuntimeException("Restaurant already exists");
        }

        RestaurantApiInfo restaurantApiInfo = restaurantApiClient.getRestaurantInfo(placeId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        if (user.getRestaurantsAdded() >= restaurantAddLimit) {
            throw new RuntimeException("Restaurant add limit reached");
        }

        String s3ImageUrl = bucketService.putObjectIntoBucket(restaurantApiInfo.getPhotoUrl(), restaurantApiInfo.getPlaceId());

        Restaurant newRestaurant = new Restaurant(
                restaurantApiInfo.getPlaceId(),
                restaurantApiInfo.getName(),
                restaurantApiInfo.getFormattedAddress(),
                restaurantApiInfo.getUrl(),
                restaurantApiInfo.getWebsite(),
                restaurantApiInfo.getFormattedPhoneNumber(),
                s3ImageUrl,
                restaurantApiInfo.getWeekdayText(),
                user,
                LocalDateTime.now()
        );

        restaurantApiInfo.setPhotoUrl(s3ImageUrl);

        user.setRestaurantsAdded(user.getRestaurantsAdded() + 1);
        userRepository.save(user);

        restaurantRepository.save(newRestaurant);
        return restaurantApiInfo;
    }

    public RestaurantApiInfo getRestaurant(String placeId, String id) {

        Restaurant restaurant = restaurantRepository.findByPlaceId(placeId).orElseThrow(
                () -> new RuntimeException("Restaurant not found")
        );

        return new RestaurantApiInfo(
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPlaceId(),
                restaurant.getLink(),
                restaurant.getWebsite(),
                restaurant.getPhoneNumber(),
                restaurant.getPhotoUrl(),
                restaurant.getWeekdayText()
        );
    }


}
