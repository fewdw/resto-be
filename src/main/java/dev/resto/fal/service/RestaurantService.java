package dev.resto.fal.service;

import dev.resto.fal.client.RestaurantApiClient;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.RestaurantApiInfo;
import dev.resto.fal.entity.User;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.response.RestaurantSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantApiClient restaurantApiClient;

    @Autowired
    private UserRepository userRepository;

    public List<RestaurantSearch> searchRestaurants(String query) {
        return restaurantApiClient.searchRestaurants(query);
    }

    public RestaurantApiInfo addRestaurant(String placeId, String userId) {
        if(restaurantRepository.existsByPlaceId(placeId)){
            throw new RuntimeException("Restaurant already exists");
        }

        RestaurantApiInfo restaurantApiInfo = restaurantApiClient.getRestaurantInfo(placeId);


        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        Restaurant newRestaurant = new Restaurant(
                restaurantApiInfo.getPlaceId(),
                restaurantApiInfo.getName(),
                restaurantApiInfo.getFormattedAddress(),
                restaurantApiInfo.getUrl(),
                restaurantApiInfo.getWebsite(),
                restaurantApiInfo.getFormattedPhoneNumber(),
                restaurantApiInfo.getPhotoUrl(),
                restaurantApiInfo.getWeekdayText(),
                user
        );
        newRestaurant = restaurantRepository.save(newRestaurant);

        return restaurantApiInfo;
        // make api request to get info
        // store the photo in s3
        // save it to db
        //return null;
    }

}
