package com.sharefile.securedoc.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharefile.securedoc.enumeration.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@JsonInclude(NON_DEFAULT)
public class RoleEntity extends Auditable {
    private String name;
    private Authority authorities;
}