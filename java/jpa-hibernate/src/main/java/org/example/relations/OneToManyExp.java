package org.example.relations;

import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;

import java.util.List;
import java.util.UUID;

public class OneToManyExp {
}

// uni-directional OneToMany
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

// bi-directional OneToMany
@Entity
class PostBi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany      //one post can have many comments
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private List<CommentsBi> comments;
}

@Entity
class CommentsBi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH) // many comments belong to one post
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private PostBi posts;
}