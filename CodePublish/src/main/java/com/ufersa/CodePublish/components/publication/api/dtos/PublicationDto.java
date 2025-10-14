package com.ufersa.CodePublish.components.publication.api.dtos;

import com.ufersa.CodePublish.commons.api.dtos.CategoryDto;
import com.ufersa.CodePublish.commons.api.dtos.ProgramingLanguageDto;
import com.ufersa.CodePublish.commons.api.dtos.TagDto;
import com.ufersa.CodePublish.components.publication.domain.entities.ProgramingLanguages;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PublicationDto {

    private Long id;

    private String title;

    private String description;

    private List<PublicationComponentDto> components;

    private CategoryDto category;

    private ProgramingLanguageDto programingLanguage;

    private Set<TagDto> tags;

    private Integer upvotesAmount;
    private Integer downvotesAmount;

    private String authorName;
}
