package com.ufersa.CodePublish.components.authentication.api.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
