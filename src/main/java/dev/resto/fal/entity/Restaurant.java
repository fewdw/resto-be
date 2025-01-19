package dev.resto.fal.entity;

import dev.resto.fal.DTO.ThumbnailRatingResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeId;

    private String name;

    private String address;

    private String link;

    private String website;

    private String phoneNumber;

    private int numberOfReviews;

    @Column(length = 512)
    private String photoUrl;

    @ElementCollection
    @CollectionTable(name = "restaurant_weekday_text", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "weekday", length = 512)
    private List<String> weekdayText;

    @ManyToOne
    @JoinColumn(name = "added_by", referencedColumnName = "id")
    private User user;

    private LocalDateTime dateAdded;

    private String username;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantRating> ratings;

    public void addRating(RestaurantRating rating) {
        this.ratings.add(rating);
    }

    public List<ThumbnailRatingResponse> getAllTagsFromRatings() {
        return ratings.stream()
                .map(ratings -> new ThumbnailRatingResponse(
                        ratings.getTag(),
                        ratings.getVotes()
                )).collect(Collectors.toList());
    }

    public Restaurant(String placeId, String name, String address, String link, String website, String phoneNumber, String photoUrl, List<String> weekdayText, User user, LocalDateTime dateAdded, String username) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.link = link;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.weekdayText = weekdayText;
        this.user = user;
        this.dateAdded = dateAdded;
        this.username = username;
    }
}