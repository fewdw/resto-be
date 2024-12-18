package dev.resto.fal.controller;

import dev.resto.fal.entity.Tag;
import dev.resto.fal.request.RatingRequest;
import dev.resto.fal.service.UserRatingService;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-rating")
public class UserRatingController {

    @Autowired
    private UserRatingService userRatingService;

    @PostMapping
    public ResponseEntity<String> leaveRating(@AuthenticationPrincipal OAuth2User principal, @RequestBody RatingRequest ratingRequest) {
        return userRatingService.leaveRating(OauthUsername.getId(principal), ratingRequest);
    }

    @GetMapping("/user-tags/{placeId}")
    public ResponseEntity<List<Tag>> getUserTags(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String placeId){
        List<Tag> response = userRatingService.getUserTags(placeId, OauthUsername.getId(principal));
        return ResponseEntity.ok(response);
    }

}
