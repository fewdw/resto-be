package dev.resto.fal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private String id;

    private String email;

    private String name;

    private String picture;

    private String username;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "user")
    private List<Favorites> favorites;

    private int numberOfRestaurantsAdded;

    public User(String id,String email, String name, String picture,String username, LocalDate createdAt, int numberOfRestaurantsAdded) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.username = username;
        this.createdAt = createdAt;
        this.numberOfRestaurantsAdded = numberOfRestaurantsAdded;
    }


}
