package org.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder           //<- in case of inheritance
@EqualsAndHashCode(callSuper = true) //<- in case of inheritance
@Entity
@Table(name = "lecture_table")
public class Lecture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID lectureId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "sectionId")
    private Section section;

    @OneToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "resourceId")
    private Resource resource;
}