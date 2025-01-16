package dev.resto.fal.response;

import dev.resto.fal.entity.RestaurantRating;
import dev.resto.fal.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantThumbnail implements Comparable<RestaurantThumbnail> {
    private String imageUrl;
    private String name;
    private String placeId;
    private String address;
    private boolean isFavorite;
    private List<ThumbnailRatingResponse> ratings;
    private LocalDateTime createdAt;
    private String restaurantUsername;

    public RestaurantThumbnail(String imageUrl, String name, String placeId, String address, boolean isFavorite, List<ThumbnailRatingResponse> ratings, String restaurantUsername) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.placeId = placeId;
        this.address = address;
        this.isFavorite = isFavorite;
        this.ratings = ratings;
        this.restaurantUsername = restaurantUsername;
    }

    @Override
    public int compareTo(RestaurantThumbnail o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}
