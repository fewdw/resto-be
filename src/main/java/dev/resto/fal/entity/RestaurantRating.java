package dev.resto.fal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_ratings")
@NoArgsConstructor
@Getter
@Setter
public class RestaurantRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    private String tagName;

    private int votes;

    String emoji;

    public RestaurantRating(Restaurant restaurant, TagType tagType, String tagName, int votes, String emoji) {
        this.restaurant = restaurant;
        this.tagType = tagType;
        this.tagName = tagName;
        this.votes = votes;
        this.emoji = emoji;
    }
}
