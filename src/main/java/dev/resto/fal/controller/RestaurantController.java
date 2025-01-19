package dev.resto.fal.controller;

import dev.resto.fal.DTO.*;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.service.RestaurantService;
import dev.resto.fal.util.Authenticate;
import dev.resto.fal.util.OauthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    Authenticate authenticate;

    @GetMapping("/search")
    public ResponseEntity<List<AddRestaurantThumbnail>> searchRestaurants(@AuthenticationPrincipal OAuth2User principal, @RequestParam(required = true) String query) throws IOException {
        authenticate.isUserAuthenticated(principal);
        List<AddRestaurantThumbnail> response = restaurantService.searchRestaurants(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Void> addRestaurant(@AuthenticationPrincipal OAuth2User principal, @RequestParam(required = true) String placeId) throws IOException {
        authenticate.isUserAuthenticated(principal);
        restaurantService.addRestaurant(placeId, OauthHelper.getId(principal));
        return ResponseEntity.ok().build();
    }

    @GetMapping("exists/{restaurantUsername}")
    public ResponseEntity<Boolean> userExistsByUsername(@AuthenticationPrincipal OAuth2User principal, @PathVariable String restaurantUsername) {
        authenticate.isUserAuthenticated(principal);
        if(!restaurantService.restaurantExistsByUsername(restaurantUsername)){
            throw new NotFoundException("Restaurant does not exist");
        }

        return ResponseEntity.ok().build();
    }

    // TODO return also user who added it :D
    @GetMapping("/{restaurantUsername}")
    public RestaurantInfoPage getRestaurant(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String restaurantUsername) throws IOException {
        return restaurantService.getRestaurant(restaurantUsername, OauthHelper.getId(principal));
    }

    //TODO: add limit of 20 restaurants
    @PostMapping("/thumbnails-search")
    public ResponseEntity<List<RestaurantThumbnailOld>> getFilteredThumbnails(@AuthenticationPrincipal OAuth2User principal, @RequestBody FilterRequest filterRequest){
        List<RestaurantThumbnailOld> restaurantThumbnails = restaurantService.getFilteredThumbnails(OauthHelper.getId(principal), filterRequest);

        return ResponseEntity.ok(restaurantThumbnails);
    }


}
