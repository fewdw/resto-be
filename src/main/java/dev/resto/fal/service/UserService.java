package dev.resto.fal.service;

import dev.resto.fal.entity.User;
import dev.resto.fal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
}
