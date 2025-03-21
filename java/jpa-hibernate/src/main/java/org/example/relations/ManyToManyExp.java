package org.example.relations;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

public class ManyToManyExp {
}

// uni-directional OneToMany
@Entity
@Table(name = "movie")
class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
}

@Entity
@Table(name = "category")
class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
        joinColumns = @JoinColumn(name = "category-id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "movie-id", referencedColumnName = "id")
    )
    private List<Movie> movies;
}

@Entity
@Table(name = "movie")
class MovieBi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
}

// bi-directional OneToMany
@Entity
@Table(name = "category")
class CategoryBi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Movie> movies;
}