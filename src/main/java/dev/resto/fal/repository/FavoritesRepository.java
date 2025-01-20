package dev.resto.fal.repository;

import dev.resto.fal.entity.Favorites;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    void deleteByUserAndRestaurant(User user, Restaurant restaurant);
    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);
    List<Restaurant> findAllByUserOrderByCreatedAtDesc(User user);
}
