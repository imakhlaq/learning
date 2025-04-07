package com.sharefile.securedoc.service.user;

import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.enumeration.LoginType;

public interface IUserService {
    void createUser(String firstName, String lastName, String password, String email);
    RoleEntity getRoleName(String name);
    void verifyToken(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    User getUserByUserId(String userId);
}