package dev.resto.fal.service;

import dev.resto.fal.DTO.RestaurantThumbnail;
import dev.resto.fal.DTO.UserFavorite;
import dev.resto.fal.entity.Favorites;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.User;
import dev.resto.fal.exceptions.ConflictException;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.repository.FavoritesRepository;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    FavoritesRepository favoritesRepository;

    @Value("${restaurant.pages-limit}")
    private int RESTAURANTS_PER_PAGE;

    @Transactional
    public void addFavorite(String userId, UserFavorite userFavorite) {

        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findByUsername(userFavorite.getRestaurantUsername()).orElseThrow(()-> new NotFoundException("Restaurant not found"));

        boolean favoriteExists = favoritesRepository.existsByUserAndRestaurant(user, restaurant);

        if (userFavorite.isFavorite() && !favoriteExists) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            favoritesRepository.save(new Favorites(user, restaurant, currentDateTime));
            userRepository.save(user);
            return;
        }

        if (!userFavorite.isFavorite() && favoriteExists) {
            favoritesRepository.deleteByUserAndRestaurant(user, restaurant);
            return;
        }

        throw new ConflictException("We could not add the restaurant to your favorites");

    }

    public List<RestaurantThumbnail> getFavorites(String username, int page) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, RESTAURANTS_PER_PAGE);

        List<Favorites> userFavorites = favoritesRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);

        return userFavorites.stream()
                .map(Favorites::getRestaurant)
                .map(restaurant -> new RestaurantThumbnail(
                        restaurant.getPhotoUrl(),
                        true,
                        restaurant.getName(),
                        restaurant.getUsername(),
                        restaurant.getAddress(),
                        restaurant.getAllTagsFromRatings()
                ))
                .toList();

    }
}
