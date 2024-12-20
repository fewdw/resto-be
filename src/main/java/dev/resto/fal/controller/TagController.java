package dev.resto.fal.controller;

import dev.resto.fal.entity.Tag;
import dev.resto.fal.request.AddTag;
import dev.resto.fal.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping
    public Tag addTag(@Valid @RequestBody AddTag tag) {
        return tagService.addTag(tag);
    }
}
