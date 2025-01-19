package dev.resto.fal.client;

import dev.resto.fal.DTO.AddRestaurantThumbnail;
import dev.resto.fal.DTO.RestaurantApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class RestaurantApiClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${google.api.key}")
    private String apiKey;

    public List<String> searchRestaurants(String query) {
        String url = String.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&key=%s", query, apiKey);
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>) response.get("predictions"))
                .flatMapMany(Flux::fromIterable)
                .filter(this::hasFoodType)
                .map(prediction -> (String) prediction.get("place_id"))
                .collectList()
                .block();
    }


    private boolean hasFoodType(Map<String, Object> prediction) {
        List<String> types = (List<String>) prediction.get("types");

        Set<String> foodTypes = Set.of("food", "bar", "meal_takeaway", "restaurant", "cafe");
        boolean isFoodPlace = !Collections.disjoint(types, foodTypes);

        boolean typesNotNull = types != null;

        return isFoodPlace && typesNotNull;
    }

    public List<AddRestaurantThumbnail> getRestaurantThumbnails(List<String> placeIds) {
        return placeIds.stream()
                .map(this::getRestaurantThumbnailInfo)
                .collect(Collectors.toList());
    }

    private AddRestaurantThumbnail getRestaurantThumbnailInfo(String placeId) {
        String url = String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&key=%s", placeId, apiKey);
        Map<String, Object> prediction = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> result = (Map<String, Object>) prediction.get("result");

        AddRestaurantThumbnail addRestaurantThumbnail = new AddRestaurantThumbnail();
        addRestaurantThumbnail.setName((String) result.get("name"));
        addRestaurantThumbnail.setAddress((String) result.get("formatted_address"));
        addRestaurantThumbnail.setPlaceId((String) result.get("place_id"));

        List<Map<String, Object>> photos = (List<Map<String, Object>>) result.get("photos");
        if (photos != null && !photos.isEmpty()) {
            String photoReference = (String) photos.get(0).get("photo_reference");
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=%s&key=%s", photoReference, apiKey);
            addRestaurantThumbnail.setImageUrl(photoUrl);
        }

        return addRestaurantThumbnail;
    }


    public RestaurantApiInfo getRestaurantInfo(String placeId) {
        String url = String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&key=%s", placeId, apiKey);
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::mapToRestaurantInfo)
                .block();
    }

    private RestaurantApiInfo mapToRestaurantInfo(Map<String, Object> prediction) {
        Map<String, Object> result = (Map<String, Object>) prediction.get("result");

        RestaurantApiInfo restaurantApiInfo = new RestaurantApiInfo();

        restaurantApiInfo.setRestaurantName((String) result.get("name"));
        restaurantApiInfo.setRestaurantAddress((String) result.get("formatted_address"));
        restaurantApiInfo.setImageUrl((String) result.get("url"));
        restaurantApiInfo.setWebsite((String) result.get("website"));
        restaurantApiInfo.setPhoneNumber((String) result.get("formatted_phone_number"));

        List<Map<String, Object>> photos = (List<Map<String, Object>>) result.get("photos");
        if (photos != null && !photos.isEmpty()) {
            String photoReference = (String) photos.get(0).get("photo_reference");
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=%s&key=%s", photoReference, apiKey);
            restaurantApiInfo.setImageUrl(photoUrl);
        }

        Map<String, Object> openingHours = (Map<String, Object>) result.get("opening_hours");
        if (openingHours != null) {
            restaurantApiInfo.setWeekdayText((List<String>) openingHours.get("weekday_text"));
        }

        return restaurantApiInfo;
    }





}
