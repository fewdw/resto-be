package dev.resto.fal.DTO;

import dev.resto.fal.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestaurantThumbnailRating implements Comparable<RestaurantThumbnailRating> {
    private Tag tag;
    private int votes;

    @Override
    public int compareTo(RestaurantThumbnailRating o) {
        return this.votes - o.votes;
    }
}
