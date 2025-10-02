package com.ufersa.CodePublish.components.publication.api.dtos;

import com.ufersa.CodePublish.commons.api.dtos.TagDto;
import com.ufersa.CodePublish.components.publication.domain.entities.Category;
import com.ufersa.CodePublish.components.publication.domain.entities.ProgramingLanguages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PublicationDto {

    private Long id;

    private String title;

    private String description;

    private ProgramingLanguages programingLanguage;

    private List<PublicationComponentDto> components;

    private Category category;

    private Set<TagDto> tags;
}
