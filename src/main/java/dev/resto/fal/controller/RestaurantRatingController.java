package dev.resto.fal.controller;

import dev.resto.fal.response.RestaurantRatingResponse;
import dev.resto.fal.service.RestaurantRatingService;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/restaurant-rating")
@RestController
public class RestaurantRatingController {

    @Autowired
    RestaurantRatingService restaurantRatingService;

    @GetMapping("/{restaurantUsername}")
    public List<RestaurantRatingResponse> getRestaurantTags(@AuthenticationPrincipal OAuth2User principal, @PathVariable String restaurantUsername) {
        return restaurantRatingService.getRestaurantTags(OauthUsername.getId(principal), restaurantUsername);
    }

}
