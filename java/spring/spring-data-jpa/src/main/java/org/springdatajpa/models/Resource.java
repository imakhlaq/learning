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
@Table(name = "resource_table")
public class Resource extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID resourceId;

    private String name;

    private Integer size;

    private String url;

    @OneToOne(mappedBy = "resource")
    private Lecture lecture;
}