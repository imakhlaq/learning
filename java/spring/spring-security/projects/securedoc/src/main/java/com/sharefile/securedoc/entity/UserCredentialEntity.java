package com.sharefile.securedoc.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_credentials")
@Entity
public class UserCredentialEntity extends Auditable {
    private String password;
    @OneToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true) //show this column as id
    @JsonProperty("user_id")
    private UserEntity userEntity;

    public UserCredentialEntity(UserEntity userEntity, String password) {
        this.password = password;
        this.userEntity = userEntity;
    }
}