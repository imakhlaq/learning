package com.sharefile.securedoc.dto;

import lombok.Data;

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
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private boolean mfa;
}