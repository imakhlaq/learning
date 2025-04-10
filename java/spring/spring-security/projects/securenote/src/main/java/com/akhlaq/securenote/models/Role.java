package com.akhlaq.securenote.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private AppRoles roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonBackReference //no circular reference while serialization
    @ToString.Exclude //no circular reference while printing
    private Set<User> users = new HashSet<>();

    public Role(AppRoles appRoles) {
        this.roleName = appRoles;
    }
}