package com.ufersa.CodePublish.components.publication.api.dtos;

import com.ufersa.CodePublish.commons.api.dtos.TagDto;
import com.ufersa.CodePublish.components.publication.domain.entities.ProgramingLanguages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePublicationDto {

    @NotBlank(message = "O título é obrigatório")
    private String title;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotNull(message = "No mínimo um arquivo precisa ser informado")
    private List<PublicationComponentDto> components;

    @NotNull(message = "A categoria precisa ser informada")
    private Long categoryId;

    @NotNull(message = "No mínimo uma linguagem de programação precisar ser informada")
    private Long programingLanguageId;

    private Set<TagDto> tags;
}
