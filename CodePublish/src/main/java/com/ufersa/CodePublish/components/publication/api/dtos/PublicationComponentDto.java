package com.ufersa.CodePublish.components.publication.api.dtos;

import com.ufersa.CodePublish.components.files.api.dtos.FileDto;
import lombok.Data;

import java.util.List;

@Data
public class PublicationComponentDto {
    private Long id;
    private FileDto file;
    private String fileId;
    private String description;
}
