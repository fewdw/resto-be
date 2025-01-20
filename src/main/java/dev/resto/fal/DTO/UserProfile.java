package dev.resto.fal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String name;
    private String picture;
    private String username;
    private boolean isOwner;
}
