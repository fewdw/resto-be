package dev.resto.fal.controller;

import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String root() {
        return "{\"RestoMtl\":\"Hello World!\"}";
    }
}
