package dev.resto.fal.client;

import dev.resto.fal.response.RestaurantApiInfo;
import dev.resto.fal.response.RestaurantSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;



@Component
public class RestaurantApiClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${google.api.key}")
    private String apiKey;

    public List<RestaurantSearch> searchRestaurants(String query) {
        String url = String.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&key=%s", query, apiKey);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>) response.get("predictions"))
                .flatMapMany(Flux::fromIterable)
                .filter(this::hasFoodType) // Filter results that have "food" in types
                .map(this::mapToRestaurantSearch)
                .collectList()
                .block();
    }

    private boolean hasFoodType(Map<String, Object> prediction) {
        List<String> types = (List<String>) prediction.get("types");
        return types != null && types.contains("food");
    }

    private RestaurantSearch mapToRestaurantSearch(Map<String, Object> prediction) {
        String name = (String) prediction.get("description");
        String placeId = (String) prediction.get("place_id");

        RestaurantSearch restaurant = new RestaurantSearch();
        restaurant.setName(name);
        restaurant.setPlaceId(placeId);

        return restaurant;
    }


    public RestaurantApiInfo getRestaurantInfo(String placeId) {
        // Build the URL for the API request using placeId and apiKey
        String url = String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&key=%s", placeId, apiKey);
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::mapToRestaurantInfo)
                .block();
    }

    public RestaurantApiInfo mapToRestaurantInfo(Map<String, Object> prediction) {
        Map<String, Object> result = (Map<String, Object>) prediction.get("result");

        RestaurantApiInfo restaurantApiInfo = new RestaurantApiInfo();

        restaurantApiInfo.setName((String) result.get("name"));
        restaurantApiInfo.setFormattedAddress((String) result.get("formatted_address"));
        restaurantApiInfo.setPlaceId((String) result.get("place_id"));
        restaurantApiInfo.setUrl((String) result.get("url"));
        restaurantApiInfo.setWebsite((String) result.get("website"));
        restaurantApiInfo.setFormattedPhoneNumber((String) result.get("formatted_phone_number"));

        List<Map<String, Object>> photos = (List<Map<String, Object>>) result.get("photos");
        if (photos != null && !photos.isEmpty()) {
            String photoReference = (String) photos.get(0).get("photo_reference");
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=%s&key=%s", photoReference, apiKey);
            restaurantApiInfo.setPhotoUrl(photoUrl);
        }

        Map<String, Object> openingHours = (Map<String, Object>) result.get("opening_hours");
        if (openingHours != null) {
            restaurantApiInfo.setWeekdayText((List<String>) openingHours.get("weekday_text"));
        }

        return restaurantApiInfo;
    }





}
