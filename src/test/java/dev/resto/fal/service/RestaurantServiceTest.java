package dev.resto.fal.service;

import dev.resto.fal.client.RestaurantApiClient;
import dev.resto.fal.entity.User;
import dev.resto.fal.exceptions.ConflictException;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.repository.FavoritesRepository;
import dev.resto.fal.repository.RestaurantRepository;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.service.BucketService;
import dev.resto.fal.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Use this to enable Mockito annotations
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantApiClient restaurantApiClient;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BucketService bucketService;

    @Mock
    private FavoritesRepository favoritesRepository;

    @InjectMocks
    private RestaurantService restaurantService;

//    @Test
//    void searchRestaurantsNonExistingUserTest() {
//        User user = new User();
//        user.setNumberOfRestaurantsAdded(20);
//        when(userRepository.findById("1")).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
//            restaurantService.searchRestaurants("1", "query");
//        });
//
//        assertEquals("User not found", exception.getMessage());
//    }

}
