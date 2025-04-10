package com.akhlaq.securenote.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Column(name = "email")
    private String email;

    @Size(max = 120)
    @Column(name = "password")
    private String password;

    private Boolean accountNonExpired = true;
    private Boolean accountNonLocked = true;
    private Boolean credentialsNonExpired = true;
    private Boolean enabled = true;

    private LocalDate creationExpireDate;
    private LocalDate accountExpireDate;

    private String jwtSecret;
    private String twoFactorSecret;
    private boolean isTwoFactorEnabled;
    private String signUpMethod;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonBackReference
    @ToString.Exclude
    private Role role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
    public User(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return false;
        if (!(o instanceof User)) return false;
        return userId != null && userId.equals(((User) o).userId);

    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}