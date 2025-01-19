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
public class ThumbnailRatingResponse {
    private Tag tag;
    private int votes;
}
