package dev.resto.fal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
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

    @Column(length = 512)
    private String photoUrl;

    @ElementCollection
    @CollectionTable(name = "restaurant_weekday_text", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "weekday", length = 512)
    private List<String> weekdayText;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantRating> ratings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "added_by", referencedColumnName = "id")
    private User user;

    public Restaurant(String placeId, String name, String address, String link, String website, String phoneNumber, String photoUrl, List<String> weekdayText, User user) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.link = link;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.weekdayText = weekdayText;
        this.user = user;
    }

}
