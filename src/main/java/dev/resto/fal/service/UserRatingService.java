package dev.resto.fal.service;

import dev.resto.fal.entity.*;
import dev.resto.fal.repository.*;
import dev.resto.fal.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRatingService {

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RestaurantRatingRepository restaurantRatingRepository;

    public ResponseEntity<String> leaveRating(String userId, RatingRequest ratingRequest) {

        // Fetching and validating user, restaurant, and tag
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Restaurant restaurant = restaurantRepository.findByPlaceId(ratingRequest.getPlaceId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Tag tag = tagRepository.findByName(ratingRequest.getTagName())
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        boolean exists = userRatingRepository.existsByUserAndRestaurantAndTag(user, restaurant, tag);

        if (!exists && ratingRequest.isLike()) {
            addRating(user, restaurant, tag);
            return ResponseEntity.ok("Rating saved");
        }

        if (exists && !ratingRequest.isLike()) {
            removeRating(user, restaurant, tag);
            return ResponseEntity.ok("Rating deleted");
        }

        if (exists && ratingRequest.isLike()) {
            return ResponseEntity.badRequest().body("Rating already exists");
        }

        if (!exists && !ratingRequest.isLike()) {
            return ResponseEntity.badRequest().body("Rating does not exist");
        }

        return ResponseEntity.badRequest().body("Something went wrong");
    }

    private void addRating(User user, Restaurant restaurant, Tag tag) {
        UserRating userRating = new UserRating(user, restaurant, tag, true);
        userRatingRepository.save(userRating);

        RestaurantRating restaurantRating = restaurantRatingRepository.findByRestaurantAndTagName(restaurant, tag.getName())
                .orElseGet(() -> new RestaurantRating(restaurant, tag.getType(), tag.getName(), 0));

        restaurantRating.setVotes(restaurantRating.getVotes() + 1);
        restaurantRatingRepository.save(restaurantRating);
    }

    private void removeRating(User user, Restaurant restaurant, Tag tag) {
        UserRating userRating = userRatingRepository.findByUserAndRestaurantAndTag(user, restaurant, tag);
        userRatingRepository.delete(userRating);

        RestaurantRating restaurantRating = restaurantRatingRepository.findByRestaurantAndTagName(restaurant, tag.getName())
                .orElseThrow(() -> new RuntimeException("Restaurant rating not found"));

        restaurantRating.setVotes(restaurantRating.getVotes() - 1);
        restaurantRatingRepository.save(restaurantRating);
    }

}
