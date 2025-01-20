package dev.resto.fal.repository;

import dev.resto.fal.entity.Favorites;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);
    List<Favorites> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    boolean existsByUserIdAndRestaurantPlaceId(String userId, String placeId);
    @Transactional
    void deleteByUserAndRestaurant(User user, Restaurant restaurant);
    boolean existsByUserIdAndRestaurantUsername(String userId, String username);
}
