package dev.resto.fal.service;

import dev.resto.fal.entity.*;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.repository.*;
import dev.resto.fal.DTO.RestaurantRatingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantRatingService {

    private final RestaurantRatingRepository restaurantRatingRepository;
    private final RestaurantRepository restaurantRepository;
    private final TagRepository tagRepository;
    private final UserRatingRepository userRatingRepository;
    private final UserRepository userRepository;

    public RestaurantRatingService(RestaurantRatingRepository restaurantRatingRepository, RestaurantRepository restaurantRepository, TagRepository tagRepository, UserRatingRepository userRatingRepository, UserRepository userRepository) {
        this.restaurantRatingRepository = restaurantRatingRepository;
        this.restaurantRepository = restaurantRepository;
        this.tagRepository = tagRepository;
        this.userRatingRepository = userRatingRepository;
        this.userRepository = userRepository;
    }

    public List<RestaurantRatingResponse> getRestaurantTags(String userId, String restaurantUsername) {

        Restaurant restaurant = restaurantRepository.findByUsername(restaurantUsername).orElseThrow(() -> new NotFoundException("Restaurant not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        List<RestaurantRating> restaurantRatings = restaurantRatingRepository.findAllByRestaurant(restaurant);

        List<UserRating> userRatings = userRatingRepository.findAllByUserAndRestaurant(user, restaurant);

        List<Tag> userTags = userRatings.stream().map(UserRating::getTag).toList();

        List<Tag> allTags = tagRepository.findAll();

        List<RestaurantRatingResponse> restaurantRatingsList = new ArrayList<>();

        for (Tag tag : allTags) {

            RestaurantRatingResponse restaurantRatingResponse = new RestaurantRatingResponse();

            restaurantRatingResponse.setId(tag.getId());
            restaurantRatingResponse.setName(tag.getName());
            restaurantRatingResponse.setTag(tag);

            int numberOfVotes = restaurantRatings.stream().filter(restaurantRating -> restaurantRating.getTag().getName().equals(tag.getName())).mapToInt(RestaurantRating::getVotes).sum();
            restaurantRatingResponse.setVotes(numberOfVotes);

            boolean voted = userTags.contains(tag);
            restaurantRatingResponse.setUserVoted(voted);

            restaurantRatingsList.add(restaurantRatingResponse);

        }

        return restaurantRatingsList;
    }
}
