package org.example.compositeFKey;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Embeddable
class StudentKey {

    String enrollmentId;
    Integer year;
}

@Entity
public class Student {

    @EmbeddedId
    StudentKey studentKey; //embedded key
}