package com.ufersa.CodePublish.components.user.domain.services.impl;

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

}
