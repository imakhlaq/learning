package com.akhlaq.securenote.services.impl;

import com.akhlaq.securenote.models.AppRoles;
import com.akhlaq.securenote.models.User;
import com.akhlaq.securenote.repositories.RoleRepo;
import com.akhlaq.securenote.repositories.UserRepo;
import com.akhlaq.securenote.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }
    @Override
    public void updateUsers(Long userId, String roleName) {
        var user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        var role = AppRoles.valueOf(roleName);
        var dbRole = roleRepo.findByRoleName(role).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(dbRole);
        userRepo.save(user);
    }
}