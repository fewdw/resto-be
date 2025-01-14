package dev.resto.fal.response;

import dev.resto.fal.entity.RestaurantRating;
import dev.resto.fal.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantThumbnail {
    private String imageUrl;
    private String name;
    private String placeId;
    private String address;
    private boolean isFavorite;
    private List<ThumbnailRatingResponse> ratings;
}
