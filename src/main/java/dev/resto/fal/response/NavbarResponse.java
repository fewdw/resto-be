package dev.resto.fal.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class NavbarResponse {
    private String name;
    private String profilePicture;
    private String username;
}
