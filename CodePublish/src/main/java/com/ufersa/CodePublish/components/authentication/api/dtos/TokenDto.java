package com.ufersa.CodePublish.components.authentication.api.dtos;

import lombok.Data;

@Data
public class TokenDto {
    private String username;
    private String accessToken;
}
