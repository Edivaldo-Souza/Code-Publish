package com.ufersa.CodePublish.commons.api.controllers;

import com.ufersa.CodePublish.commons.ApiResponse;
import com.ufersa.CodePublish.commons.ResponseUtil;
import com.ufersa.CodePublish.commons.api.dtos.TagDto;
import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.commons.domain.services.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagDto>>> get(HttpServletRequest request) {
        List<Tag> tags = tagService.getAll();
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = new TagDto();
            tagDto.setName(tag.getName());
            tagDtos.add(tagDto);
        }

        ApiResponse<List<TagDto>> apiResponse = ResponseUtil
                .success(tagDtos,"Tags encontradas",request.getRequestURI());

        return ResponseEntity.ok(apiResponse);
    }
}
