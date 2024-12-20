package dev.resto.fal.controller;

import dev.resto.fal.response.RestaurantApiInfo;
import dev.resto.fal.request.UserFavorite;
import dev.resto.fal.response.NavbarResponse;
import dev.resto.fal.service.UserService;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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

    //TODO: add limit of 20
    @GetMapping("/favorites")
    public ResponseEntity<Set<RestaurantApiInfo>> getFavorites(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(userService.getFavorites(OauthUsername.getId(principal)));
    }

    //TODO: add limit of 20
    @GetMapping("/favorites/{userId}")
    public ResponseEntity<Set<RestaurantApiInfo>> getFavorites(@PathVariable(required = true) String userId) {
        return ResponseEntity.ok(userService.getFavorites(userId));
    }

    // get restaurants added by user
    @GetMapping("/restaurants-added")
    public ResponseEntity<Set<RestaurantApiInfo>> getRestaurantsAdded(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(userService.getRestaurantsAdded(OauthUsername.getId(principal)));
    }

    // get restaurants added by any user
    @GetMapping("/restaurants-added/{userId}")
    public ResponseEntity<Set<RestaurantApiInfo>> getRestaurantsAdded(@PathVariable(required = true) String userId) {
        return ResponseEntity.ok(userService.getRestaurantsAdded(userId));
    }

}
