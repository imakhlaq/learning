package org.springdatajpa.models;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data //== @Getter, @Setter, @ToString, @RequiredConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID authorId;// if its null hibernate will assign the value to it
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

    //owner of relationship (in code only. it will not have foreign key on dB)
    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = @JoinColumn(
            name = "author_id",
            referencedColumnName = "authorId"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "course_id",
            referencedColumnName = "courseId"
        )
    )
    List<Course> courses;

    @PostConstruct
    private void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PostUpdate
    private void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}