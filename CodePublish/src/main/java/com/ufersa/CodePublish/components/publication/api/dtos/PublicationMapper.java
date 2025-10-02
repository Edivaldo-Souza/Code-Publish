package com.ufersa.CodePublish.components.publication.api.dtos;

import com.ufersa.CodePublish.commons.api.dtos.TagDto;
import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.components.publication.domain.entities.Category;
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
            publicationDto.setProgramingLanguage(ProgramingLanguages.valueOf(
                    publication.getProgramingLanguage()));
        }

        if(publication.getCategory()!=null){
            publicationDto.setCategory(Category.fromValue(publication.getCategory()));
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

        return publicationDto;
    }

    public Publication CreatePublicationDtoToPublication(CreatePublicationDto createPublicationDto) {
        Publication publication = new Publication();

        publication.setTitle(createPublicationDto.getTitle());
        publication.setDescription(createPublicationDto.getDescription());
        publication.setProgramingLanguage(createPublicationDto.getProgramingLanguage().name());
        publication.setCategory(createPublicationDto.getCategory().getValue());

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
        publication.setProgramingLanguage(updatePublicationDto.getProgramingLanguage().name());
        publication.setCategory(updatePublicationDto.getCategory().getValue());

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
