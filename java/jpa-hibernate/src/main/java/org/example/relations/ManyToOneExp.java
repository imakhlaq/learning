package org.example.relations;

import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;

import java.util.List;
import java.util.UUID;

public class ManyToOneExp {
}

// uni-directional ManyToOne
@Entity
@Table(name = "comments")
class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne  // Many comments belong to one post
    @JoinColumn(name = "post_id", referencedColumnName = "postId")//references to the id of Post
    private Post posts;
}

@Entity
@Table(name = "user_Post")
class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID postId;

}

// bi-directional OneToMany
@Entity
class CommentsBi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH) // many comments belong to one post
    @JoinColumn(name = "post_id", referencedColumnName = "postId")
    private PostBi posts;
}

@Entity
class PostBi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postId;

    @OneToMany(mappedBy = "posts")      //one post can have many comments
    private List<CommentsBi> comments;
}