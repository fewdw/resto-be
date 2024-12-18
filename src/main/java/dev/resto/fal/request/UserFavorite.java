package dev.resto.fal.request;

import dev.resto.fal.entity.FavoriteAction;
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
