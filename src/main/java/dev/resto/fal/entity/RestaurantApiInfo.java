package dev.resto.fal.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestaurantApiInfo {

    private String name;
    private String formattedAddress;
    private String placeId;
    private String url;
    private String website;
    private String formattedPhoneNumber;
    private String photoUrl;
    private List<String> weekdayText;

}
