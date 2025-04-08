package com.sharefile.securedoc.utils;

import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.entity.UserCredentialEntity;
import com.sharefile.securedoc.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UserUtils {

    public static UserEntity createUserEntity(String firstName, String lastName,
                                              String email, RoleEntity role) {
        return UserEntity.builder()
            .userId(UUID.randomUUID().toString())
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .role(role)
            .lastLogin(LocalDateTime.now())
            .loginAttempts(0)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .accountNonLocked(true)
            .enable(false)
            .bio(EMPTY)
            .phoneNumber(EMPTY)
            .qrCodeSecret(EMPTY)
            .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
            .build();
    }

    public static User fromUserEntity(UserEntity userEntity, RoleEntity role,
                                      UserCredentialEntity userCredentialEntity) {
        var user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialExpired(userCredentialEntity));
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRole(role.getName());
        user.setAuthorities(role.getAuthorities().getValue());
        return user;
    }
    public static Boolean isCredentialExpired(UserCredentialEntity userCredentialEntity) {
        return userCredentialEntity.getCreatedAt().plusDays(90).isBefore(LocalDateTime.now());
    }
}