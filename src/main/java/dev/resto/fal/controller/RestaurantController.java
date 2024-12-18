package dev.resto.fal.controller;

import dev.resto.fal.entity.RestaurantApiInfo;
import dev.resto.fal.response.RestaurantSearch;
import dev.resto.fal.service.RestaurantService;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
