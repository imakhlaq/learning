package org.example.relations;

import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

public class OneToManyExp {
}

// uni directional OneToMany
@Entity
@Table(name = "user_Post")
class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID id;

}

@Entity
@Table(name = "comments")
class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne  // A many comments belongs to one post
    @JoinColumn(name = "post_id", referencedColumnName = "id")//references to the id of Post
    private Post posts;
}