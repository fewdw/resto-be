package dev.resto.fal.service;

import dev.resto.fal.entity.*;
import dev.resto.fal.exceptions.ConflictException;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.repository.*;
import dev.resto.fal.DTO.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<Void> leaveRating(String userId, RatingRequest ratingRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Restaurant restaurant = restaurantRepository.findByUsername(ratingRequest.getRestaurantUsername())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        Tag tag = tagRepository.findByName(ratingRequest.getTagName())
                .orElseThrow(() -> new NotFoundException("Tag not found"));

        boolean exists = userRatingRepository.existsByUserAndRestaurantAndTag(user, restaurant, tag);

        if (!exists && ratingRequest.isLike()) {
            addRating(user, restaurant, tag);
            return ResponseEntity.ok().build();
        }

        if (exists && !ratingRequest.isLike()) {
            removeRating(user, restaurant, tag);
            return ResponseEntity.ok().build();
        }

        if (exists && ratingRequest.isLike()) {
            throw new ConflictException("Rating already exists");
        }

        if (!exists && !ratingRequest.isLike()) {
            throw new ConflictException("Rating does not exist");
        }

        throw new ConflictException("Something went wrong");
    }

    private void addRating(User user, Restaurant restaurant, Tag tag) {
        UserRating userRating = new UserRating(user, restaurant, tag);
        userRatingRepository.save(userRating);

        RestaurantRating restaurantRating = restaurantRatingRepository.findByRestaurantAndTagName(restaurant, tag.getName())
                .orElseGet(() -> new RestaurantRating(restaurant, tag, 0));

        restaurantRating.setVotes(restaurantRating.getVotes() + 1);
        restaurant.setNumberOfReviews(restaurant.getNumberOfReviews() + 1);
        restaurant.addRating(restaurantRating);
        restaurantRepository.save(restaurant);
    }

    private void removeRating(User user, Restaurant restaurant, Tag tag) {
        UserRating userRating = userRatingRepository.findByUserAndRestaurantAndTag(user, restaurant, tag);
        userRatingRepository.delete(userRating);

        RestaurantRating restaurantRating = restaurantRatingRepository.findByRestaurantAndTagName(restaurant, tag.getName())
                .orElseThrow(() -> new RuntimeException("Restaurant rating not found"));

        restaurantRating.setVotes(restaurantRating.getVotes() - 1);
        restaurant.setNumberOfReviews(restaurant.getNumberOfReviews() - 1);
        restaurantRepository.save(restaurant);

        if (restaurantRating.getVotes() <= 0) {
            restaurantRatingRepository.delete(restaurantRating);
            return;
        }

        restaurantRatingRepository.save(restaurantRating);
    }

}
