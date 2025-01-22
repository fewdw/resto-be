package dev.resto.fal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantSearchAutocomplete {
    private String placeId;
    private String description;
    private String username;
    private boolean isAdded;
    private boolean isLikedByUser;

}
