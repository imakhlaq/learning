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
@Table(name = "section_table")
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sectionId;

    private String name;

    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private Course course;

    @OneToMany(mappedBy = "section")
    private List<Lecture> lectures;
}