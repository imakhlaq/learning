package com.akhlaq.securenote.services;

import com.akhlaq.securenote.models.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    void updateUsers(Long userId, String roleName);
}