package dev.resto.fal.controller;

import dev.resto.fal.DTO.RestaurantThumbnail;
import dev.resto.fal.DTO.UserFavorite;
import dev.resto.fal.service.FavoritesService;
import dev.resto.fal.util.Authenticate;
import dev.resto.fal.util.OauthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final Authenticate authenticate;
    private final FavoritesService favoritesService;

    public FavoritesController(Authenticate authenticate, FavoritesService favoritesService) {
        this.authenticate = authenticate;
        this.favoritesService = favoritesService;
    }

    @PostMapping("/favorite")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal OAuth2User principal, @RequestBody UserFavorite userFavorite) {
        authenticate.isUserAuthenticated(principal);
        favoritesService.addFavorite(OauthHelper.getId(principal), userFavorite);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getFavorites(@AuthenticationPrincipal OAuth2User principal,
                                                                  @PathVariable(required = true) String username,
                                                                  @PathVariable(required = true) int page) {

        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(favoritesService.getFavorites(username, page));
    }
}
