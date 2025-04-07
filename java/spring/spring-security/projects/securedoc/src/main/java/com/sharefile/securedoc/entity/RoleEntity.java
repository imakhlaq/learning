package com.sharefile.securedoc.entity;

import com.sharefile.securedoc.enumeration.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_roles")
@Entity
public class RoleEntity extends Auditable {
    private String name;
    private Authority authorities;
}