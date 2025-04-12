package com.sharefile.securedoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharefile.securedoc.enumeration.AuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class UserEntity extends Auditable {
    @Column(unique = true, nullable = false, updatable = false)
    private String userId; //this will be the UUID that will be sent in the request
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false, updatable = false)
    private String email;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private String phone;
    private String bio;
    private String imageUrl;
    /*
    =================
    SPRING SECURITY FIELDS
    =================
     */
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    @Column(columnDefinition = "boolean default false")
    private Boolean mfa;
    /*
   =================
   MFA FIELDS
   =================
    */
    @JsonIgnore //ignore this property while deserialization
    private String qrCodeSecret;
    @Column(columnDefinition = "text")//JPA uses varchar256 by default
    private String qrCodeImageUri;

    /*
    =================
    Social logins
    =================
  */
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
    /*
    =================
    USER AND USER_ROLES
    =================
  */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private RoleEntity role;
}