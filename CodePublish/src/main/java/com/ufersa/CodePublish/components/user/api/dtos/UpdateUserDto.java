package com.ufersa.CodePublish.components.user.api.dtos;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String username;
    private String email;
    private String password;
}
