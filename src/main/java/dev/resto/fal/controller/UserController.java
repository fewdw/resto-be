package dev.resto.fal.controller;

import dev.resto.fal.DTO.RestaurantThumbnail;
import dev.resto.fal.DTO.UserProfile;
import dev.resto.fal.entity.User;
import dev.resto.fal.service.UserService;
import dev.resto.fal.util.Authenticate;
import dev.resto.fal.util.OauthHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final Authenticate authenticate;

    public UserController(UserService userService, Authenticate authenticate) {
        this.userService = userService;
        this.authenticate = authenticate;
    }

    @GetMapping("/auth")
    public ResponseEntity<Void> isAuthenticated(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(@AuthenticationPrincipal OAuth2User principal) {
        User user = authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getProfile(user));
    }

    @GetMapping("/profile/{profileUsername}")
    public ResponseEntity<UserProfile> getUserProfile(@AuthenticationPrincipal OAuth2User principal,
                                                      @PathVariable(required = true) String profileUsername) {
        User user = authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getUserProfile(user, profileUsername));
    }

    @GetMapping("/added/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getRestaurantsAdded(@AuthenticationPrincipal OAuth2User principal,
                                                                         @PathVariable(required = true) int page) {

        User user = authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getRestaurantsAddedById(user, page));
    }

    @GetMapping("/added/{username}/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getRestaurantsAddedByUsername(@AuthenticationPrincipal OAuth2User principal,
                                                                         @PathVariable(required = true) String username,
                                                                         @PathVariable(required = true) int page) {

        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getRestaurantsAddedByUsername(username, page));
    }



}
