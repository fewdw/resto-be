package dev.resto.fal.DTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantThumbnailOld {
    private String photoUrl;
    private String name;
    private String placeId;
    private String address;
    private boolean contains;
    private List<RestaurantThumbnailRating> allTagsFromRatings;
    private LocalDateTime dateAdded;
    private String username;
}
