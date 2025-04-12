package com.sharefile.securedoc.dto;

import com.sharefile.securedoc.enumeration.AuthProvider;
import lombok.Data;

import java.util.Map;

@Data
public class User {
    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private String userId; //this will be the UUID that will be sent in the request
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String bio;
    private String imageUrl;
    private String qrCodeImageUri;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String role;
    private String authorities;
    private AuthProvider provider;
    private String providerId;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private Boolean mfa;
    private Map<String, Object> attributes;
}