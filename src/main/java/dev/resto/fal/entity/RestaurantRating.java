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
    private Tag tag;

    private int votes;

}