package com.ufersa.CodePublish.components.files.api.dtos;

import com.ufersa.CodePublish.components.files.domain.entities.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public File fileDtoToFile(FileDto fileDto) {
        File file = new File();
        file.setId(fileDto.getId());
        file.setName(fileDto.getName());
        file.setType(fileDto.getType());
        file.setUrl(fileDto.getUrl());
        return file;
    }

    public FileDto fileToFileDto(File file) {
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setName(file.getName());
        fileDto.setType(file.getType());
        fileDto.setUrl(file.getUrl());
        return fileDto;
    }
}
