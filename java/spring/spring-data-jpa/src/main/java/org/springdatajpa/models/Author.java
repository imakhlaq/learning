package org.springdatajpa.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data //== @Getter, @Setter, @ToString, @RequiredConstructor
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder           //<- in case of inheritance
@EqualsAndHashCode(callSuper = true) //<- in case of inheritance
@Entity
@Table(name = "author")
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID authorId;// if its null hibernate will assign the value to it
    private String firstName;
    private String lastName;

    @Column(name = "user_email", unique = true, nullable = false, length = 50)
    private String email;

    private Integer age;

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
    private List<Course> courses;

}