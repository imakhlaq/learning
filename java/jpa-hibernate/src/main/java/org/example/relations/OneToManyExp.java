package org.example.relations;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "classroom-id", referencedColumnName = "id")
    private List<Student> students;

    @PrePersist
    public void prePersist() {
        LocalDateTime.now();
        //createdBy = LoggedUser.get();
    }

    @PreUpdate
    public void preUpdate() {
        LocalDateTime.now();
        //updatedBy = LoggedUser.get();
    }

}

@Entity
@Table
class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
}

// bi-directional OneToMany
@Entity
@Table
class classRoomBi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idkk;
    private String name;

    @OneToMany(mappedBy = "classRoom", fetch = FetchType.EAGER)
    private List<StudentBi> students;

}

@Entity
@Table
class StudentBi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classroom-id", referencedColumnName = "idkk")
    private classRoomBi classRoom;

}