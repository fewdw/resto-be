package dev.resto.fal.response;

import dev.resto.fal.entity.TagType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantRatingResponse {
    private Long id;
    private String name;
    private TagType type;
    private String emoji;
    private int votes;
    private boolean userVoted;
}
