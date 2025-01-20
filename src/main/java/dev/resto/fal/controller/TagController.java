package dev.resto.fal.controller;

import dev.resto.fal.entity.Tag;
import dev.resto.fal.service.TagService;
import dev.resto.fal.util.Authenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final Authenticate authenticate;
    private final TagService tagService;

    public TagController(Authenticate authenticate, TagService tagService) {
        this.authenticate = authenticate;
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags(@AuthenticationPrincipal OAuth2User principal) {
        authenticate.isUserAuthenticated(principal);
        return tagService.getAllTags();
    }

}
