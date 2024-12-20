package dev.resto.fal.response;

import dev.resto.fal.entity.RestaurantRating;

import java.util.Set;

public class RestaurantThumbnail {
    private String imageUrl;
    private String name;
    private String placeId;
    private Set<RestaurantRating> ratings;
}
