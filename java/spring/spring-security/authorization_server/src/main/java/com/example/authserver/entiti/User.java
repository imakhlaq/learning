package com.example.authserver.entiti;

import com.example.authserver.entiti.security.Authority;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        joinColumns = @JoinColumn(name = "authority_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Authority> authority;

}