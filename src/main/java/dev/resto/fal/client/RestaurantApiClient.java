package dev.resto.fal.client;

import dev.resto.fal.DTO.AddRestaurantThumbnail;
import dev.resto.fal.DTO.RestaurantApiInfo;
import dev.resto.fal.DTO.RestaurantSearchAutocomplete;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.repository.FavoritesRepository;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class RestaurantApiClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${google.api.key}")
    private String apiKey;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;

    public List<RestaurantSearchAutocomplete> searchRestaurants(String query, String userId) {
        String url = String.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&key=%s", query, apiKey);
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>) response.get("predictions"))
                .flatMapMany(Flux::fromIterable)
                .filter(this::hasFoodType) // Filter predictions to include only relevant types
                .map(prediction -> mapToRestaurantSearchAutocomplete(prediction, userId))
                .collectList()
                .block();
    }


    private RestaurantSearchAutocomplete mapToRestaurantSearchAutocomplete(Map<String, Object> prediction, String userId) {
        RestaurantSearchAutocomplete autocomplete = new RestaurantSearchAutocomplete();
        autocomplete.setPlaceId((String) prediction.get("place_id"));
        autocomplete.setDescription((String) prediction.get("description"));
        autocomplete.setAdded(false);
        autocomplete.setUsername(null);

        Optional<Restaurant> restaurant = restaurantRepository.findByPlaceId(autocomplete.getPlaceId());


        if(restaurant.isPresent()) {
            autocomplete.setAdded(true);
            autocomplete.setUsername(restaurant.get().getUsername());
            autocomplete.setLikedByUser(favoritesRepository.existsByUserIdAndRestaurantUsername(userId, restaurant.get().getUsername()));
        }

        return autocomplete;
    }


    private boolean hasFoodType(Map<String, Object> prediction) {
        List<String> types = (List<String>) prediction.get("types");
        if (types == null) return false;

        Set<String> foodTypes = Set.of("food", "bar", "meal_takeaway", "restaurant", "cafe");
        return !Collections.disjoint(types, foodTypes);
    }


    public List<AddRestaurantThumbnail> getRestaurantThumbnails(String userId, List<String> placeIds, List<String> existingPlaceIds) {
        return placeIds.stream()
                .map(placeId -> getRestaurantThumbnailInfo(userId, placeId, existingPlaceIds))
                .collect(Collectors.toList());
    }

    private AddRestaurantThumbnail getRestaurantThumbnailInfo(String userId, String placeId, List<String> existingPlaceIds) {
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
        addRestaurantThumbnail.setAdded(existingPlaceIds.contains(addRestaurantThumbnail.getPlaceId()));
        addRestaurantThumbnail.setLikedByUser(favoritesRepository.existsByUserIdAndRestaurantPlaceId(userId, addRestaurantThumbnail.getPlaceId()));

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
                .map(response -> mapToRestaurantInfo(response, placeId))
                .block();
    }

    private RestaurantApiInfo mapToRestaurantInfo(Map<String, Object> prediction, String placeId) {
        Map<String, Object> result = (Map<String, Object>) prediction.get("result");

        RestaurantApiInfo restaurantApiInfo = new RestaurantApiInfo();

        restaurantApiInfo.setRestaurantName((String) result.get("name"));
        restaurantApiInfo.setRestaurantAddress((String) result.get("formatted_address"));
        restaurantApiInfo.setImageUrl((String) result.get("url"));
        restaurantApiInfo.setWebsite((String) result.get("website"));
        restaurantApiInfo.setPhoneNumber((String) result.get("formatted_phone_number"));
        restaurantApiInfo.setPlaceId(placeId);

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
