package dev.resto.fal.DTO;

import dev.resto.fal.entity.Tag;
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
