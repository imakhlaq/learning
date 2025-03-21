package org.example.relations;

import jakarta.persistence.*;

import java.util.UUID;

public class OneToOneExp {
}

//uni-directional one-to-one relationship
@Entity
class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

}

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_account", referencedColumnName = "id")
    private Account account;

}

// bi-directional one-to-one relationship
@Entity
class AccountBi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //doesn't of the relationship
    @OneToOne(mappedBy = "account")
    private User user;
}

@Entity
class UserBi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //Owner of the relationship
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_account", referencedColumnName = "id")
    private Account account;
}