package dev.resto.fal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Restaurant(String placeId, String name, String address, String link, String website, String phoneNumber, String photoUrl, List<String> weekdayText, User user, LocalDateTime dateAdded) {
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
    }



}
