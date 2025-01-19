package dev.resto.fal.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestaurantApiInfo {

    private String restaurantName;
    private String restaurantAddress;
    private String placeId;
    private String googleMapUrl;
    private String website;
    private String phoneNumber;
    private String imageUrl;
    private List<String> weekdayText;
    private String restaurantUsername;

}
