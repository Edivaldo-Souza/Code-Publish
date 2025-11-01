package com.ufersa.CodePublish.components.user.api.restcontrollers;

import com.ufersa.CodePublish.commons.ApiResponse;
import com.ufersa.CodePublish.commons.ResponseUtil;
import com.ufersa.CodePublish.components.authentication.api.dtos.LoginDto;
import com.ufersa.CodePublish.components.authentication.api.dtos.TokenDto;
import com.ufersa.CodePublish.components.authentication.domain.services.AuthenticationService;
import com.ufersa.CodePublish.components.authentication.domain.services.TokenService;
import com.ufersa.CodePublish.components.user.api.dtos.CreateUserDto;
import com.ufersa.CodePublish.components.user.api.dtos.UpdateUserDto;
import com.ufersa.CodePublish.components.user.api.dtos.UserDto;
import com.ufersa.CodePublish.components.user.api.dtos.UserMapper;
import com.ufersa.CodePublish.components.user.domain.entities.User;
import com.ufersa.CodePublish.components.user.domain.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<UserDto>> getCurrent(
            @CookieValue("accessToken") String token,
            HttpServletRequest request
    ){
        User user = userService.getCurrentUser(token);
        UserDto userDto = userMapper.userToUserDto(user);

        ApiResponse<UserDto> response = ResponseUtil
                .success(userDto,"Usuário encontrado",request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAll(HttpServletRequest request) {
        List<UserDto> users = userService.getAll().stream().map(userMapper::userToUserDto).toList();

        ApiResponse<List<UserDto>> response =
                ResponseUtil.success(users,"Usuários encontrados",request.getRequestURI());

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getById(
            HttpServletRequest request,
            @PathVariable Long id) throws Exception {
        UserDto user = userMapper.userToUserDto(userService.getById(id));

        ApiResponse<UserDto> response = ResponseUtil
                .success(user,"Usuário encontrado",request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> create(
            HttpServletRequest request,
            @RequestBody CreateUserDto createUserDto) throws Exception {
        User newUser = userMapper.createUserDtoToUser(createUserDto);
        UserDto userDto = userMapper.userToUserDto(userService.create(newUser));

        ApiResponse<UserDto> response = ResponseUtil
                .success(userDto,"Usuário cadastrado",request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserDto>> updateCurrent(
            HttpServletRequest request,
            @RequestBody UpdateUserDto updateUserDto,
            @CookieValue("accessToken") String token
    ) throws Exception {
        User updatedUser = userMapper.updateUserDtoToUser(updateUserDto);
        UserDto userDto = userMapper.userToUserDto(userService.updateCurrent(token,updatedUser));

        String newToken = tokenService.generateToken(userDto.getEmail());

        ResponseCookie cookie = ResponseCookie.from("accessToken",newToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(28800)
                .build();

        ApiResponse<UserDto> response = ResponseUtil
                .success(userDto,"Usuário atualizado",request.getRequestURI());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> update(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody UpdateUserDto updateUserDto) throws Exception {
        User updatedUser = userMapper.updateUserDtoToUser(updateUserDto);
        UserDto userDto = userMapper.userToUserDto(userService.update(id,updatedUser));

        ApiResponse<UserDto> response = ResponseUtil
                .success(userDto,"Usuário atualizado",request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            HttpServletRequest request,
            @PathVariable Long id) throws Exception {
        userService.delete(id);

        ApiResponse<Void> response = ResponseUtil
                .success(null,"Usuário deletado",request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
