package dev.resto.fal.response;

import dev.resto.fal.entity.Tag;
import dev.resto.fal.enums.TagType;
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
    private Tag tag;
    private int votes;
    private boolean userVoted;
}
