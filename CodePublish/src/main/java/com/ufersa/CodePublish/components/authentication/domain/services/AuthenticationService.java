package com.ufersa.CodePublish.components.authentication.domain.services;

import com.ufersa.CodePublish.components.authentication.api.dtos.LoginDto;
import com.ufersa.CodePublish.components.authentication.api.dtos.TokenDto;
import com.ufersa.CodePublish.components.user.domain.entities.User;
import com.ufersa.CodePublish.components.user.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public TokenDto signin(LoginDto loginDto) {
        var email = loginDto.getEmail();
        var password = loginDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        Optional<User> user = userRepository.getByEmail(email);
        TokenDto tokenDto = new TokenDto();
        if(user.isPresent()) {
            tokenDto.setUsername(user.get().getUsername());
        }
        tokenDto.setToken(tokenService.generateToken(email));

        return tokenDto;
    }
}
