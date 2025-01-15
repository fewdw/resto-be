package dev.resto.fal.service;

import dev.resto.fal.enums.FavoriteAction;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.User;
import dev.resto.fal.exceptions.UserNotFoundException;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.request.UserFavorite;
import dev.resto.fal.response.NavbarResponse;
import dev.resto.fal.response.RestaurantThumbnail;
import dev.resto.fal.response.UserProfileResponse;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public boolean userExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean userExistsByUsername(String username){return userRepository.existsByUsername(username);}

    public void addUser(User newUser) {
        userRepository.save(newUser);
    }

    public NavbarResponse getNavbar(OAuth2User principal) {
        String userId = OauthUsername.getId(principal);
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new NavbarResponse(user.getName(), user.getPicture(), user.getUsername());
    }

    public ResponseEntity<String> addFavorite(String userId, UserFavorite userFavorite) {

        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findByPlaceId(userFavorite.getPlaceId()).orElseThrow(()-> new RuntimeException("Restaurant not found"));// TODO HANDLE CUSTOM

        if (userFavorite.getAction() == FavoriteAction.ADD && !user.getFavorites().contains(restaurant)) {
            user.getFavorites().add(restaurant);
            userRepository.save(user);
            return ResponseEntity.ok().body("Restaurant added to favorites");
        }

        if (userFavorite.getAction() == FavoriteAction.REMOVE && user.getFavorites().contains(restaurant)) {
            user.getFavorites().remove(restaurant);
            userRepository.save(user);
            return ResponseEntity.ok().body("Restaurant removed");
        }

        return ResponseEntity.badRequest().body("Something went wrong");

    }

    public List<RestaurantThumbnail> getFavorites(String userId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getFavorites().stream()
                .map(restaurant -> new RestaurantThumbnail(
                        restaurant.getPhotoUrl(),
                        restaurant.getName(),
                        restaurant.getPlaceId(),
                        restaurant.getAddress(),
                        userRepository.findById(userId).get().getFavorites().contains(restaurant),
                        restaurant.getAllTagsFromRatings(),
                        restaurant.getDateAdded()
                ))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<RestaurantThumbnail> getRestaurantsAdded(String userId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Restaurant> restaurants = restaurantRepository.findAllByUser(user);

        return restaurants.stream()
                .map(restaurant -> new RestaurantThumbnail(
                        restaurant.getPhotoUrl(),
                        restaurant.getName(),
                        restaurant.getPlaceId(),
                        restaurant.getAddress(),
                        userRepository.findById(userId).get().getFavorites().contains(restaurant),
                        restaurant.getAllTagsFromRatings(),
                        restaurant.getDateAdded()
                ))
                .sorted()
                .collect(Collectors.toList());
    }

    public UserProfileResponse getProfile(String userId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserProfileResponse(user.getName(), user.getPicture(), user.getUsername());

    }
}
