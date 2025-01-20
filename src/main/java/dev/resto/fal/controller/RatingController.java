package dev.resto.fal.controller;

import dev.resto.fal.DTO.RatingRequest;
import dev.resto.fal.DTO.RestaurantRatingResponse;
import dev.resto.fal.service.RestaurantRatingService;
import dev.resto.fal.service.UserRatingService;
import dev.resto.fal.util.Authenticate;
import dev.resto.fal.util.OauthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private UserRatingService userRatingService;

    @Autowired
    RestaurantRatingService restaurantRatingService;

    @Autowired
    Authenticate authenticate;

    @GetMapping("/{restaurantUsername}")
    public List<RestaurantRatingResponse> getRestaurantTags(@AuthenticationPrincipal OAuth2User principal, @PathVariable String restaurantUsername) {
        authenticate.isUserAuthenticated(principal);
        return restaurantRatingService.getRestaurantTags(OauthHelper.getId(principal), restaurantUsername);
    }

    @PostMapping
    public ResponseEntity<Void> leaveRating(@AuthenticationPrincipal OAuth2User principal, @RequestBody RatingRequest ratingRequest) {
        authenticate.isUserAuthenticated(principal);
        return userRatingService.leaveRating(OauthHelper.getId(principal), ratingRequest);
    }

}
