package dev.resto.fal.controller;

import dev.resto.fal.entity.User;
import dev.resto.fal.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    private final UserRepository userRepository;

    public RootController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String root() {
        return "{\"RestoMtl\":\"Hello World!\"}";
    }
}
