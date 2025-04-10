package com.sharefile.securedoc.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents") //Naming the table
@JsonInclude(NON_DEFAULT)
public class DocumentEntity extends Auditable {
    @Column(updatable = false, unique = true, nullable = false) // We cannot have a user without an id
    private String documentId;
    private String name;
    private String description;
    private String uri;
    private long size;
    private String formattedSize; // Human-readable representation of size.
    private String icon;
    private String extension;
    // We need to make sure every document have a owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id", // Let's make the id a foreign key
        foreignKey = @ForeignKey(name = "fk_documents_owner", foreignKeyDefinition = "foreign key /* FK */ (user_id) references UserEntity", value = ConstraintMode.CONSTRAINT)
    )
    private UserEntity owner;
}