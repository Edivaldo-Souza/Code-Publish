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
public class UpdatePublicationDto {
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String title;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotNull(message = "No mínimo uma linguagem de programação precisar ser informada")
    private ProgramingLanguages programingLanguage;

    @NotNull(message = "No mínimo um arquivo precisa ser informado")
    private List<PublicationComponentDto> components;

    @NotNull(message = "A categoria precisa ser informada")
    private Category category;

    private Set<TagDto> tags;
}
