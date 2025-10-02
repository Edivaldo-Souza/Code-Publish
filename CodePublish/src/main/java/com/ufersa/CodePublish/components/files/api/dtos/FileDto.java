package com.ufersa.CodePublish.components.files.api.dtos;

import lombok.Data;

@Data
public class FileDto {
    private Long id;

    private String name;

    private String type;

    private String url;
}
