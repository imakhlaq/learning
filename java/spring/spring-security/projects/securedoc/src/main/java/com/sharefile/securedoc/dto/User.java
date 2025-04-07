package com.sharefile.securedoc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharefile.securedoc.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String userId; //this will be the UUID that will be sent in the request
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String bio;
    private String imageUrl;
    private String qrCodeImageUri;
    private String role;
    private String authorities;
    private String lastLogin;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean enabled;
    private Boolean enable;
    private Boolean credentialsNonExpired;
    private String createdAt;
    private String updatedAt;
    private Long createdBy;
    private Long updatedBy;
}