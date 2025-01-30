package dev.resto.fal.controller;

import dev.resto.fal.DTO.*;
import dev.resto.fal.entity.User;
import dev.resto.fal.service.RestaurantService;
import dev.resto.fal.util.Authenticate;
import dev.resto.fal.util.OauthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final Authenticate authenticate;

    public RestaurantController(RestaurantService restaurantService, Authenticate authenticate) {
        this.restaurantService = restaurantService;
        this.authenticate = authenticate;
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantSearchAutocomplete>> searchRestaurants(@AuthenticationPrincipal OAuth2User principal,
                                                                          @RequestParam(required = true) String query) throws IOException {

        User user = authenticate.isUserAuthenticated(principal);
        List<RestaurantSearchAutocomplete> response = restaurantService.searchRestaurants(user, query);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<RestaurantThumbnail> addRestaurant(@AuthenticationPrincipal OAuth2User principal,
                                                             @RequestParam(required = true) String placeId) throws IOException {

        User user = authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(restaurantService.addRestaurant(placeId, user));
    }

    @GetMapping("/{restaurantUsername}")
    public ResponseEntity<RestaurantInfoPage> getRestaurant(@AuthenticationPrincipal OAuth2User principal,
                                                            @PathVariable(required = true) String restaurantUsername) throws IOException {
        User user = authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(restaurantService.getRestaurant(user, restaurantUsername));
    }

    @PostMapping("/thumbnails-search/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getFilteredThumbnails(@AuthenticationPrincipal OAuth2User principal,
                                                                           @RequestBody FilterRequest filterRequest,
                                                                           @PathVariable int page) {
        User user = authenticate.isUserAuthenticated(principal);
        List<RestaurantThumbnail> restaurantThumbnails = restaurantService.getFilteredThumbnails(user, filterRequest, page);
        return ResponseEntity.ok(restaurantThumbnails);
    }

    @GetMapping("/new/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getNewThumbnails(@AuthenticationPrincipal OAuth2User principal,
                                                                      @PathVariable int page) {
        User user = authenticate.isUserAuthenticated(principal);
        List<RestaurantThumbnail> restaurantThumbnails = restaurantService.getNewThumbnails(user, page);
        return ResponseEntity.ok(restaurantThumbnails);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Pagination> getNewThumbnailsPagination(@AuthenticationPrincipal OAuth2User principal) {
        authenticate.isUserAuthenticated(principal);
        return ResponseEntity.ok(restaurantService.newAndPopularPagination());
    }

    @GetMapping("/popular/{page}")
    public ResponseEntity<List<RestaurantThumbnail>> getPopularThumbnails(@AuthenticationPrincipal OAuth2User principal,
                                                                          @PathVariable int page) {
        User user = authenticate.isUserAuthenticated(principal);
        List<RestaurantThumbnail> restaurantThumbnails = restaurantService.getPopularThumbnails(user, page);
        return ResponseEntity.ok(restaurantThumbnails);
    }


}
