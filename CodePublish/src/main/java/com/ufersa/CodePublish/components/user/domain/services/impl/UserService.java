package com.ufersa.CodePublish.components.user.domain.services.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ufersa.CodePublish.components.authentication.domain.services.TokenService;
import com.ufersa.CodePublish.components.user.domain.entities.User;
import com.ufersa.CodePublish.components.user.domain.entities.UserType;
import com.ufersa.CodePublish.components.user.domain.repositories.UserRepository;
import com.ufersa.CodePublish.components.user.domain.services.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public User getById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new Exception("Usuário id= "+id+" não encontrado");
        }
        return user.get();
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User create(User user) throws Exception {

        Optional<User> userWithSameUsername = userRepository.findByUsername(user.getUsername());
        if (userWithSameUsername.isPresent()) {
            throw new Exception("Usuário com o nome de usuário: "+user.getUsername()
            + " já existente");
        }

        Optional<User> userWithSameEmail = userRepository.getByEmail(user.getEmail());
        if (userWithSameEmail.isPresent()) {
            throw new Exception("Usuário com o email: "+user.getEmail()+" já existente");
        }

        user.setRole(UserType.STANDARD.name());

        String newPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(newPassword);

        user.setPublicationAmount(0);
        user.setRatingAmount(0);

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) throws Exception {

        Optional<User> userWithSameUsername = userRepository.findByUsername(user.getUsername());
        if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
            throw new Exception("Usuário com o nome de usuário: "+user.getUsername()
                    + " já existente");
        }

        Optional<User> userWithSameEmail = userRepository.getByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
            throw new Exception("Usuário com o email: "+user.getEmail()+" já existente");
        }

        Optional<User> currentUser = userRepository.findById(id);
        if (currentUser.isPresent()) {
            user.setRatingAmount(currentUser.get().getRatingAmount());
            user.setPublicationAmount(currentUser.get().getPublicationAmount());
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new Exception("Usuário com id: "+id+" não encontrado");
        }
        userRepository.delete(user.get());
    }

    public User getCurrentUser(String token){
        DecodedJWT jwt = tokenService.decodeToken(token);
        Optional<User> user = userRepository.getByEmail(jwt.getSubject());

        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }
}
