package dev.resto.fal.service;

import dev.resto.fal.entity.*;
import dev.resto.fal.repository.*;
import dev.resto.fal.response.RestaurantRatingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantRatingService {

    @Autowired
    private RestaurantRatingRepository restaurantRatingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private UserRepository userRepository;


    public List<RestaurantRatingResponse> getRestaurantTags(String userId, String placeId) {

        Restaurant restaurant = restaurantRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found"));

        List<RestaurantRating> restaurantRatings = restaurantRatingRepository.findAllByRestaurant(restaurant);

        List<UserRating> userRatings = userRatingRepository.findAllByUserAndRestaurant(user, restaurant);
        List<Tag> userTags = userRatings.stream().map(UserRating::getTag).toList();

        List<Tag> allTags = tagRepository.findAll();

        List<RestaurantRatingResponse> restaurantRatingsList = new ArrayList<>();

        for (Tag tag : allTags) {

            RestaurantRatingResponse restaurantRatingResponse = new RestaurantRatingResponse();

            restaurantRatingResponse.setId(tag.getId());
            restaurantRatingResponse.setName(tag.getName());
            restaurantRatingResponse.setType(tag.getType());
            restaurantRatingResponse.setEmoji(tag.getEmoji());

            int numberOfVotes = restaurantRatings.stream().filter(restaurantRating -> restaurantRating.getTagName().equals(tag.getName())).mapToInt(RestaurantRating::getVotes).sum();
            restaurantRatingResponse.setVotes(numberOfVotes);

            boolean voted = userTags.contains(tag);
            restaurantRatingResponse.setUserVoted(voted);

            restaurantRatingsList.add(restaurantRatingResponse);

        }

        return restaurantRatingsList;


    }
}
