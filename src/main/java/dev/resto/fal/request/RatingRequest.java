package dev.resto.fal.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RatingRequest {

    private String placeId;
    private String tagName;
    @JsonProperty("is_like")
    private boolean isLike;
}
