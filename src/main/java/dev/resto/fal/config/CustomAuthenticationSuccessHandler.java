package dev.resto.fal.config;

import dev.resto.fal.entity.User;
import dev.resto.fal.service.UserService;
import dev.resto.fal.util.UsernameGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.url}")
    String frontendUrl;

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String id = (String) oAuth2User.getAttributes().get("sub");
        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");
        String picture = (String) oAuth2User.getAttributes().get("picture");

        String username = UsernameGenerator.generateRandomUsername();
        while (userService.userExistsByUsername(username)) {
            username = UsernameGenerator.generateRandomUsername();
        }

        if (!userService.userExists(email)) {
            User newUser = new User(id, email, name, picture, username, LocalDate.now(), 0);
            userService.addUser(newUser);
        }

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .build().toUriString();
        response.sendRedirect(redirectUrl);
    }


}
