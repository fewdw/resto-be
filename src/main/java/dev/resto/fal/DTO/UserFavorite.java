package dev.resto.fal.DTO;

import dev.resto.fal.enums.FavoriteAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavorite {
    FavoriteAction action;
    String placeId;
}
