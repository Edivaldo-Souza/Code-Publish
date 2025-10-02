package com.ufersa.CodePublish.components.publication.api.dtos;

import com.ufersa.CodePublish.components.files.api.dtos.FileMapper;
import com.ufersa.CodePublish.components.publication.domain.entities.PublicationComponent;
import org.springframework.stereotype.Component;

@Component
public class PublicationComponentMapper {
    private final FileMapper fileMapper;

    public PublicationComponentMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public PublicationComponent publicationComponentDtoToPublicationComponent(PublicationComponentDto dto) {
        PublicationComponent publicationComponent = new PublicationComponent();
        publicationComponent.setId(dto.getId());
        publicationComponent.setDescription(dto.getDescription());
        publicationComponent.setFileId(dto.getFileId());
        if(dto.getFile()!=null){
            publicationComponent.setFile(fileMapper.fileDtoToFile(dto.getFile()));
        }

        return publicationComponent;
    }

    public PublicationComponentDto publicationComponentToDto(PublicationComponent publicationComponent) {
        PublicationComponentDto publicationComponentDto = new PublicationComponentDto();
        publicationComponentDto.setId(publicationComponent.getId());
        publicationComponentDto.setDescription(publicationComponent.getDescription());
        if(publicationComponent.getFile()!=null){
            publicationComponentDto.setFile(fileMapper.fileToFileDto(publicationComponent.getFile()));
        }

        return publicationComponentDto;
    }
}
