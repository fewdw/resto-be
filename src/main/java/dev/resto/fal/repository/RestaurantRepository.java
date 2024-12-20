package dev.resto.fal.repository;

import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByPlaceId(String placeId);
    Optional<Restaurant> findByPlaceId(String placeId);
    List<Restaurant> findAllByUser(User user);
}
