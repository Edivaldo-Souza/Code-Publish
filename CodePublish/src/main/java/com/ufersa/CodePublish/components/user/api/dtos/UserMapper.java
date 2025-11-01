package com.ufersa.CodePublish.components.user.api.dtos;

import com.ufersa.CodePublish.components.user.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setTotalRatings(user.getRatingAmount());
        userDto.setTotalPublications(user.getPublicationAmount());
        return userDto;
    }

    public User createUserDtoToUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());

        return user;
    }

    public User updateUserDtoToUser(UpdateUserDto updateUserDto) {
        User user = new User();
        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        user.setPassword(updateUserDto.getPassword());
        return user;
    }
}
