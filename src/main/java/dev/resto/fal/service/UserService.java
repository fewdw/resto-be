package dev.resto.fal.service;

import dev.resto.fal.DTO.RestaurantThumbnailOld;
import dev.resto.fal.enums.FavoriteAction;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.User;
import dev.resto.fal.exceptions.ConflictException;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.DTO.UserFavorite;
import dev.resto.fal.DTO.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public boolean userExists(String email){ return userRepository.findByEmail(email).isPresent(); }

    public boolean userExistsByUsername(String username){ return userRepository.existsByUsername(username); }

    public void addUser(User newUser) { userRepository.save(newUser); }

    public UserProfile getProfile(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return new UserProfile(user.getName(), user.getPicture(), user.getUsername(), true);
    }

    public UserProfile getUserProfile(String requesterId, String profileUsername){

        User requester = userRepository.findById(requesterId).orElseThrow(() -> new NotFoundException("User not found"));
        User user = userRepository.findByUsername(profileUsername).orElseThrow(() -> new NotFoundException("User not found"));

        return new UserProfile(user.getName(), user.getPicture(), user.getUsername(), user.equals(requester));
    }

    public ResponseEntity<Void> addFavorite(String userId, UserFavorite userFavorite) {

        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findByUsername(userFavorite.getRestaurantUsername()).orElseThrow(()-> new NotFoundException("Restaurant not found"));

        if (userFavorite.getAction() == FavoriteAction.ADD && !user.getFavorites().contains(restaurant)) {
            user.getFavorites().add(restaurant);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }

        if (userFavorite.getAction() == FavoriteAction.REMOVE && user.getFavorites().contains(restaurant)) {
            user.getFavorites().remove(restaurant);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }

        throw new ConflictException("We could not add the restaurant to your favorites");

    }

    public List<RestaurantThumbnailOld> getFavorites(String userId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return user.getFavorites().stream()
                .map(restaurant -> new RestaurantThumbnailOld(
                        restaurant.getPhotoUrl(),
                        restaurant.getName(),
                        restaurant.getPlaceId(),
                        restaurant.getAddress(),
                        userRepository.findById(userId).get().getFavorites().contains(restaurant),
                        restaurant.getAllTagsFromRatings(),
                        restaurant.getDateAdded(),
                        restaurant.getUsername()
                ))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<RestaurantThumbnailOld> getRestaurantsAdded(String userId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        List<Restaurant> restaurants = restaurantRepository.findAllByUser(user);

        return restaurants.stream()
                .map(restaurant -> new RestaurantThumbnailOld(
                        restaurant.getPhotoUrl(),
                        restaurant.getName(),
                        restaurant.getPlaceId(),
                        restaurant.getAddress(),
                        userRepository.findById(userId).get().getFavorites().contains(restaurant),
                        restaurant.getAllTagsFromRatings(),
                        restaurant.getDateAdded(),
                        restaurant.getUsername()

                ))
                .sorted()
                .collect(Collectors.toList());
    }
}
