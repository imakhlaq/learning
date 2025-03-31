package com.example.authserver.entiti.security;

import com.example.authserver.entiti.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    @ManyToMany(mappedBy = "authority")
    private Set<User> users;
}