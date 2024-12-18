package dev.resto.fal.repository;

import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.RestaurantRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRatingRepository extends JpaRepository<RestaurantRating, Long> {
    boolean existsByRestaurantAndTagName(Restaurant restaurant, String tagName);
    Optional<RestaurantRating> findByRestaurantAndTagName(Restaurant restaurant, String tagName);
}
