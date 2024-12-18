package dev.resto.fal.controller;

import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.RestaurantApiInfo;
import dev.resto.fal.request.UserFavorite;
import dev.resto.fal.response.NavbarResponse;
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

    @GetMapping("/favorites")
    public ResponseEntity<Set<RestaurantApiInfo>> getFavorites(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(userService.getFavorites(OauthUsername.getId(principal)));
    }

}
