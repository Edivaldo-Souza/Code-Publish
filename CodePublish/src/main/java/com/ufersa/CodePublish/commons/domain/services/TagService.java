package com.ufersa.CodePublish.commons.domain.services;

import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.commons.domain.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAll(){
        return tagRepository.findAll();
    }
}
