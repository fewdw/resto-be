package dev.resto.fal.service;

import dev.resto.fal.entity.FavoriteAction;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.RestaurantApiInfo;
import dev.resto.fal.entity.User;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.request.UserFavorite;
import dev.resto.fal.response.NavbarResponse;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public void addUser(User newUser) {
        userRepository.save(newUser);
    }

    public NavbarResponse getNavbar(OAuth2User principal) {
        String userId = OauthUsername.getId(principal);
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new NavbarResponse(user.getName(), user.getPicture());
    }

    public ResponseEntity<String> addFavorite(String userId, UserFavorite userFavorite) {

        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findByPlaceId(userFavorite.getPlaceId()).orElseThrow(()-> new RuntimeException("Restaurant not found"));

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

    public Set<RestaurantApiInfo> getFavorites(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getFavorites().stream()
                .map(restaurant -> new RestaurantApiInfo(
                        restaurant.getName(),
                        restaurant.getAddress(),
                        restaurant.getPlaceId(),
                        restaurant.getLink(),
                        restaurant.getWebsite(),
                        restaurant.getPhoneNumber(),
                        restaurant.getPhotoUrl(),
                        restaurant.getWeekdayText()
                ))
                .collect(Collectors.toSet());
    }

}
