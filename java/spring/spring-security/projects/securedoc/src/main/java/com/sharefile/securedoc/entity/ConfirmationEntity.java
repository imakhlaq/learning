package com.sharefile.securedoc.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "confirmations")
@Entity
public class ConfirmationEntity extends Auditable {
    private String key;
    @OneToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true) //show this column as id
    @JsonProperty("user_id")
    private UserEntity userEntity;

    /*
   ==================
   Generate new UUID for Key for each object created
   ==================
    */
    public ConfirmationEntity(UserEntity userEntity) {
        this.key = UUID.randomUUID().toString();
        this.userEntity = userEntity;
    }
}