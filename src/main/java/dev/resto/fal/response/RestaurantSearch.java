package dev.resto.fal.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSearch {
    String name;
    String placeId;


    @Override
    public String toString() {
        return "RestaurantSearch{" +
                "name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                '}';
    }
}
