package dev.resto.fal.controller;

import dev.resto.fal.response.RestaurantApiInfo;
import dev.resto.fal.request.FilterRequest;
import dev.resto.fal.response.RestaurantSearch;
import dev.resto.fal.response.RestaurantThumbnail;
import dev.resto.fal.service.RestaurantService;
import dev.resto.fal.util.OauthUsername;
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

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantSearch>> searchRestaurants(@AuthenticationPrincipal OAuth2User principal, @RequestParam(required = true) String query){
        List<RestaurantSearch> response = restaurantService.searchRestaurants(query, OauthUsername.getId(principal));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public RestaurantApiInfo addRestaurant(@AuthenticationPrincipal OAuth2User principal, @RequestParam(required = true) String placeId) throws IOException {
        return restaurantService.addRestaurant(placeId, OauthUsername.getId(principal));
    }

    @GetMapping("/{placeId}")
    public RestaurantApiInfo getRestaurant(@AuthenticationPrincipal OAuth2User principal, @PathVariable(required = true) String placeId) throws IOException {
        return restaurantService.getRestaurant(placeId, OauthUsername.getId(principal));
    }

    //TODO: add limit of 20 restaurants
    @PostMapping("/thumbnails-search")
    public ResponseEntity<List<RestaurantThumbnail>> getFilteredThumbnails(@AuthenticationPrincipal OAuth2User principal, @RequestBody FilterRequest filterRequest){
        List<RestaurantThumbnail> restaurantThumbnails = restaurantService.getFilteredThumbnails(OauthUsername.getId(principal), filterRequest);

        return ResponseEntity.ok(restaurantThumbnails);
    }


}
