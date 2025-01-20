package dev.resto.fal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RatingRequest {

    private String restaurantUsername;
    private String tagName;
    @JsonProperty("is_like")
    private boolean isLike;
}
