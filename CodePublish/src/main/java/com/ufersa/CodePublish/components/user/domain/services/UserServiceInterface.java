package com.ufersa.CodePublish.components.user.domain.services;

import com.ufersa.CodePublish.components.user.domain.entities.User;

import java.util.List;

public interface UserServiceInterface {
    List<User> getAll();
    User getById(Long id) throws Exception;
    User getByUsername(String username) throws Exception;
    User create(User user) throws Exception;
    User update(Long id,User user) throws Exception;
    void delete(Long id) throws Exception;
}
