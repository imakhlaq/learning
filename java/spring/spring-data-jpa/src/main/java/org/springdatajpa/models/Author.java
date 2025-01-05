package org.springdatajpa.models;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data //== @Getter, @Setter, @ToString, @AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;// if its null hibernate will assign the value to it
    private String firstName;
    private String lastName;

    @Column(name = "user_email", unique = true, nullable = false, length = 50)
    private String email;

    private Integer age;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PostConstruct
    private void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PostUpdate
    private void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}