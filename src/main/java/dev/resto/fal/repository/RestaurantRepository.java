package dev.resto.fal.repository;

import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {
    boolean existsByPlaceId(String placeId);
    Optional<Restaurant> findByUsername(String placeId);
    List<Restaurant> findAllByUserOrderByDateAddedDesc(User user, Pageable pageable);
    boolean existsByUsername(String username);
    Optional<Restaurant> findByPlaceId(String placeId);

    @Query("SELECT r FROM Restaurant r ORDER BY r.dateAdded DESC")
    List<Restaurant> findAllOrderByDateAddedDesc(Pageable pageable);

    @Query("SELECT r FROM Restaurant r LEFT JOIN r.ratings ratings GROUP BY r ORDER BY COUNT(ratings) DESC")
    List<Restaurant> findAllOrderByNumberOfReviewsDesc(Pageable pageable);

}
