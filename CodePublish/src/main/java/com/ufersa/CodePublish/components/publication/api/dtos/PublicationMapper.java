package com.ufersa.CodePublish.components.publication.api.dtos;

import com.ufersa.CodePublish.commons.api.dtos.CategoryDto;
import com.ufersa.CodePublish.commons.api.dtos.ProgramingLanguageDto;
import com.ufersa.CodePublish.commons.api.dtos.TagDto;
import com.ufersa.CodePublish.commons.domain.entities.Category;
import com.ufersa.CodePublish.commons.domain.entities.ProgramingLanguage;
import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.components.publication.domain.entities.ProgramingLanguages;
import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PublicationMapper {

    private final PublicationComponentMapper publicationComponentMapper;

    public PublicationDto publicationToPublicationDto(Publication publication) {
        PublicationDto publicationDto = new PublicationDto();

        publicationDto.setId(publication.getId());
        publicationDto.setTitle(publication.getTitle());
        publicationDto.setDescription(publication.getDescription());

        if(publication.getProgramingLanguage()!=null){
            ProgramingLanguageDto programingLanguageDto = new ProgramingLanguageDto();
            programingLanguageDto.setId(publication.getProgramingLanguage().getId());
            programingLanguageDto.setName(publication.getProgramingLanguage().getName());
            publicationDto.setProgramingLanguage(programingLanguageDto);
        }

        if(publication.getCategory()!=null){
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(publication.getCategory().getId());
            categoryDto.setName(publication.getCategory().getName());
            publicationDto.setCategory(categoryDto);
        }

        if(publication.getComponents()!=null &&
           Hibernate.isInitialized(publicationDto.getComponents())) {
            publicationDto.setComponents(publication.getComponents()
               .stream().map(publicationComponentMapper::publicationComponentToDto).toList());
        }

        if(publication.getTags()!=null){
            Set<TagDto> tagsDto = new HashSet<>();
            for(Tag tag : publication.getTags()) {
                TagDto tagDto = new TagDto();
                tagDto.setName(tag.getName());
                tagsDto.add(tagDto);
            }
            publicationDto.setTags(tagsDto);
        }

        publicationDto.setUpvotesAmount(publication.getUpvotesAmount());
        publicationDto.setDownvotesAmount(publication.getDownvotesAmount());

        publicationDto.setAuthorName(publication.getUser().getUsername());
        return publicationDto;
    }

    public Publication CreatePublicationDtoToPublication(CreatePublicationDto createPublicationDto) {
        Publication publication = new Publication();

        publication.setTitle(createPublicationDto.getTitle());
        publication.setDescription(createPublicationDto.getDescription());
        if(createPublicationDto.getProgramingLanguageId()!=null){
            ProgramingLanguage programingLanguage = new ProgramingLanguage();
            programingLanguage.setId(createPublicationDto.getProgramingLanguageId());
            publication.setProgramingLanguage(programingLanguage);
        }

        if(createPublicationDto.getCategoryId()!=null){
            Category category = new Category();
            category.setId(createPublicationDto.getCategoryId());
            publication.setCategory(category);
        }

        if(createPublicationDto.getComponents()!=null) {
            publication.setComponents(createPublicationDto.getComponents()
                    .stream().map(publicationComponentMapper::publicationComponentDtoToPublicationComponent)
                    .toList());
        }

        if(createPublicationDto.getTags()!=null){
            Set<Tag> tags = new HashSet<>();
            for(TagDto tagDto : createPublicationDto.getTags()) {
                Tag tag = new Tag();
                tag.setName(tagDto.getName());
                tags.add(tag);
            }
            publication.setTags(tags);
        }

        return publication;
    }

    public Publication UpdatePublicationDtoToPublication(UpdatePublicationDto updatePublicationDto) {
        Publication publication = new Publication();

        publication.setTitle(updatePublicationDto.getTitle());
        publication.setDescription(updatePublicationDto.getDescription());

        if(updatePublicationDto.getProgramingLanguageId()!=null){
            ProgramingLanguage programingLanguage = new ProgramingLanguage();
            programingLanguage.setId(updatePublicationDto.getProgramingLanguageId());
            publication.setProgramingLanguage(programingLanguage);
        }

        if(updatePublicationDto.getCategoryId()!=null){
            Category category = new Category();
            category.setId(updatePublicationDto.getCategoryId());
            publication.setCategory(category);
        }

        if(updatePublicationDto.getComponents()!=null) {
            publication.setComponents(updatePublicationDto.getComponents()
                    .stream().map(publicationComponentMapper::publicationComponentDtoToPublicationComponent)
                    .toList());
        }

        if(updatePublicationDto.getTags()!=null){
            Set<Tag> tags = new HashSet<>();
            for(TagDto tagDto : updatePublicationDto.getTags()) {
                Tag tag = new Tag();
                tag.setName(tagDto.getName());
                tags.add(tag);
            }
            publication.setTags(tags);
        }

        return publication;
    }
}
