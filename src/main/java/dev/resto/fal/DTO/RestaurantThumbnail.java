package dev.resto.fal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantThumbnail{

    private String restaurantImage;
    private boolean isLikedByUser;
    private String restaurantName;
    private String restaurantUsername;
    private String restaurantAddress;
    private List<RestaurantThumbnailRating> ratings;

}
