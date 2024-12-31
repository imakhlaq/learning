package org.example.embedding;

import jakarta.persistence.*;

import java.util.UUID;

@Embeddable
class Account {

}

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uuid;

    @Embedded
    Account account;
}