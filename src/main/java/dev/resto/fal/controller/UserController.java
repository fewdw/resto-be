package dev.resto.fal.controller;

import dev.resto.fal.response.RestaurantApiInfo;
import dev.resto.fal.request.UserFavorite;
import dev.resto.fal.response.NavbarResponse;
import dev.resto.fal.response.RestaurantThumbnail;
import dev.resto.fal.response.UserProfileResponse;
import dev.resto.fal.service.UserService;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/navbar")
    public ResponseEntity<NavbarResponse> getNavbar(@AuthenticationPrincipal OAuth2User principal) {
        NavbarResponse navbarResponse = userService.getNavbar(principal);
        return ResponseEntity.ok(navbarResponse);
    }

    @PostMapping("/favorite")
    public ResponseEntity<String> addFavorite(@AuthenticationPrincipal OAuth2User principal, @RequestBody UserFavorite userFavorite) {
        userService.addFavorite(OauthUsername.getId(principal), userFavorite);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String username) {
        return ResponseEntity.ok(userService.getProfile(OauthUsername.getId(principal), username));
    }

    //TODO: add limit of 20
    @GetMapping("/favorites/{username}")
    public ResponseEntity<List<RestaurantThumbnail>> getFavorites(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String username) {
        return ResponseEntity.ok(userService.getFavorites(OauthUsername.getId(principal), username));
    }


    //TODO: add limit of 20
    @GetMapping("/restaurants-added/{username}")
    public ResponseEntity<List<RestaurantThumbnail>> getRestaurantsAdded(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String username) {
        return ResponseEntity.ok(userService.getRestaurantsAdded(OauthUsername.getId(principal), username));
    }



}
