package dev.resto.fal.service;

import dev.resto.fal.entity.User;
import dev.resto.fal.repository.UserRepository;
import dev.resto.fal.response.NavbarResponse;
import dev.resto.fal.util.OauthUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean userExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public void addUser(User newUser) {
        userRepository.save(newUser);
    }

    public NavbarResponse getNavbar(OAuth2User principal) {

        String userId = OauthUsername.getId(principal);
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new NavbarResponse(user.getName(), user.getPicture());
    }
}
