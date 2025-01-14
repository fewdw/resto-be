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
public class RestaurantRating implements Comparable<RestaurantRating>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    private int votes;

    public RestaurantRating(Restaurant restaurant, Tag tag, int votes) {
        this.restaurant = restaurant;
        this.tag = tag;
        this.votes = votes;
    }

    @Override
    public int compareTo(RestaurantRating o) {
        return this.votes - o.votes;
    }
}
