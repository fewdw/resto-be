package dev.resto.fal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavorite {
    @JsonProperty("is_favorite")
    boolean isFavorite;
    String restaurantUsername;
}
