package dev.resto.fal.controller;

import dev.resto.fal.DTO.RatingRequest;
import dev.resto.fal.service.UserRatingService;
import dev.resto.fal.util.OauthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-rating")
public class UserRatingController {

    @Autowired
    private UserRatingService userRatingService;

    @PostMapping
    public ResponseEntity<String> leaveRating(@AuthenticationPrincipal OAuth2User principal, @RequestBody RatingRequest ratingRequest) {
        return userRatingService.leaveRating(OauthHelper.getId(principal), ratingRequest);
    }

}
