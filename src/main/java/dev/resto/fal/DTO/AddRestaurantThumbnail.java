package dev.resto.fal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddRestaurantThumbnail {
    private String name;
    private String address;
    private String imageUrl;
    private String placeId;
    private boolean added;
    private boolean likedByUser;
}
