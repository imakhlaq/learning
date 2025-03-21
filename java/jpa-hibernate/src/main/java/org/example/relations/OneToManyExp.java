package org.example.relations;

import jakarta.persistence.*;

public class OneToManyExp {
}

// uni-directional OneToMany
@Entity
@Table
class classRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

}

@Entity
@Table
class student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

}

// bi-directional OneToMany