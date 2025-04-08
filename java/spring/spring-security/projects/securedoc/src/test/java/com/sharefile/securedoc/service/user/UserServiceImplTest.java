package com.sharefile.securedoc.service.user;

import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.entity.UserCredentialEntity;
import com.sharefile.securedoc.entity.UserEntity;
import com.sharefile.securedoc.enumeration.Authority;
import com.sharefile.securedoc.repository.CredentialRepo;
import com.sharefile.securedoc.repository.UserRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private CredentialRepo credentialRepo;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Test find user by ID")
    void getUserByUserIdTest() {
        //1 Arrange (arrange arguments for methods you want to test)
        var userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("junior");
        userEntity.setLastName("test");
        userEntity.setCreatedAt(LocalDateTime.of(1990, 11, 1, 1, 1));
        userEntity.setUpdatedAt(LocalDateTime.of(1990, 11, 1, 1, 1));
        userEntity.setLastLogin(LocalDateTime.of(1990, 11, 1, 1, 1));

        var roleEntity = new RoleEntity("USER", Authority.USER);
        userEntity.setRole(roleEntity);

        var credentialEntity = new UserCredentialEntity();
        credentialEntity.setUpdatedAt(LocalDateTime.of(1990, 11, 1, 1, 1));
        credentialEntity.setPassword("password");
        credentialEntity.setUser(userEntity);

        //inside the method when another method is called then what that method suppose to return when receiving a specific argument
        when(userRepo.findByUserId("1")).thenReturn(Optional.of(userEntity));
        when(credentialRepo.getUserCredentialEntityByUser_Id(1L)).thenReturn(Optional.of(credentialEntity));

        //2 Act - when you call the method
        var userByUserId = userServiceImpl.getUserByUserId("1");

        //3 Assert - what are you expecting after acting
        assertThat(userByUserId.getFirstName()).isEqualTo(userEntity.getFirstName());

    }
}