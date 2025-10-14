package com.ufersa.CodePublish.components.user.api.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private Integer totalRatings;
    private Integer totalPublications;
}
