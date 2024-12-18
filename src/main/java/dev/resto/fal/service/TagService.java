package dev.resto.fal.service;

import dev.resto.fal.entity.Tag;
import dev.resto.fal.entity.TagType;
import dev.resto.fal.repository.TagRepository;
import dev.resto.fal.request.AddTag;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag addTag(AddTag tag) {
        return tagRepository.save(new Tag(tag.getName(), tag.getType()));
    }
}