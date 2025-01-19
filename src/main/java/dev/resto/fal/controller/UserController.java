package dev.resto.fal.controller;

import dev.resto.fal.DTO.UserFavorite;
import dev.resto.fal.DTO.NavbarResponse;
import dev.resto.fal.DTO.RestaurantThumbnailOld;
import dev.resto.fal.DTO.UserProfileResponse;
import dev.resto.fal.service.UserService;
import dev.resto.fal.util.OauthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/checkAuth")
    public ResponseEntity<String> checkAuth(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(OauthHelper.getId(principal));
    }

    @GetMapping("/navbar")
    public ResponseEntity<NavbarResponse> getNavbar(@AuthenticationPrincipal OAuth2User principal) {
        NavbarResponse navbarResponse = userService.getNavbar(principal);
        return ResponseEntity.ok(navbarResponse);
    }

    @GetMapping("exists/{username}")
    public ResponseEntity<Boolean> userExistsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.userExistsByUsername(username));
    }

    @PostMapping("/favorite")
    public ResponseEntity<String> addFavorite(@AuthenticationPrincipal OAuth2User principal, @RequestBody UserFavorite userFavorite) {
        userService.addFavorite(OauthHelper.getId(principal), userFavorite);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String username) {
        return ResponseEntity.ok(userService.getProfile(OauthHelper.getId(principal), username));
    }

    //TODO: add limit of 20
    @GetMapping("/favorites/{username}")
    public ResponseEntity<List<RestaurantThumbnailOld>> getFavorites(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String username) {
        return ResponseEntity.ok(userService.getFavorites(OauthHelper.getId(principal), username));
    }


    //TODO: add limit of 20
    @GetMapping("/restaurants-added/{username}")
    public ResponseEntity<List<RestaurantThumbnailOld>> getRestaurantsAdded(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String username) {
        return ResponseEntity.ok(userService.getRestaurantsAdded(OauthHelper.getId(principal), username));
    }



}
