package dev.resto.fal.repository;

import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.Tag;
import dev.resto.fal.entity.User;
import dev.resto.fal.entity.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    boolean existsByUserAndRestaurantAndTag(User user, Restaurant restaurant, Tag tag);

    UserRating findByUserAndRestaurantAndTag(User user, Restaurant restaurant, Tag tag);
    List<UserRating> findAllByUserAndRestaurant(User user, Restaurant restaurant);
}
