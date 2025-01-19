package dev.resto.fal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantInfoPage {
    private RestaurantApiInfo restaurantApiInfo;
    private UserInfoAddedBy userInfoAddedBy;
}
