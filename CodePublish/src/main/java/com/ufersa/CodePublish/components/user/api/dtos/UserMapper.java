package com.ufersa.CodePublish.components.user.api.dtos;

import com.ufersa.CodePublish.components.user.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
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

        return user;
    }
}
