package dev.resto.fal.repository;

import dev.resto.fal.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByPlaceId(String placeId);
    Optional<Restaurant> findByPlaceId(String placeId);
}
