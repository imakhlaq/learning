package org.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder           //<- in case of inheritance
@EqualsAndHashCode(callSuper = true) //<- in case of inheritance
@Entity
@Table(name = "course_table")
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID courseId;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Author> authors;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Section> sections;
}