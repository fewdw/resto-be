package dev.resto.fal.controller;

import dev.resto.fal.DTO.RestaurantThumbnail;
import dev.resto.fal.DTO.UserFavorite;
import dev.resto.fal.DTO.UserProfile;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.service.UserService;
import dev.resto.fal.util.Authenticate;
import dev.resto.fal.util.OauthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private Authenticate authenticate;

    @Value("${restaurant.pages-limit}")
    private int RESTAURANTS_PER_PAGE;

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(@AuthenticationPrincipal OAuth2User principal) {
        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getProfile(OauthHelper.getId(principal)));
    }

    @GetMapping("/profile/{profileUsername}")
    public ResponseEntity<UserProfile> getUserProfile(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String profileUsername) {
        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getUserProfile(OauthHelper.getId(principal), profileUsername));
    }

    @GetMapping("exists/{username}")
    public ResponseEntity<Void> userExistsByUsername(@AuthenticationPrincipal OAuth2User principal, @PathVariable String username) {
        authenticate.isUserAuthenticated(principal);
        if(!userService.userExistsByUsername(username)){
            throw new NotFoundException("User does not exist");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favorite")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal OAuth2User principal, @RequestBody UserFavorite userFavorite) {
        authenticate.isUserAuthenticated(principal);
        userService.addFavorite(OauthHelper.getId(principal), userFavorite);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites/{username}/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getFavorites(@AuthenticationPrincipal OAuth2User principal,
                                                                  @PathVariable(required = true) String username,
                                                                  @PathVariable(required = true) int page) {

        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getFavorites(username, page));
    }


    @GetMapping("/added/{username}/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getRestaurantsAdded(@AuthenticationPrincipal OAuth2User principal,
                                                                         @PathVariable(required = true) String username,
                                                                         @PathVariable(required = true) int page) {

        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(userService.getRestaurantsAdded(username, page));
    }



}
