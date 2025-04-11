package com.sharefile.securedoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sharefile.securedoc.domain.RequestContext;
import com.sharefile.securedoc.exception.ApiException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

/*
All the subclasses can inherit these properties so you don't have to repeat it.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {
    @Id
    @SequenceGenerator(name = "primary_key_seq", allocationSize = 1, sequenceName = "primary_key_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_key_seq")
    @Column(name = "id", updatable = false)
    // this will be our primary key (indexing on number is much better than UUID)(DO not send this id in the response)
    private Long id;
    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();
    @NotNull
    private Long createdBy;
    @NotNull
    private Long updatedBy;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @NotNull
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @CreatedDate
    private LocalDateTime updatedAt;
    /**
     * ======================
     * This method is called before an entity is persisted in the database.
     * It sets the createdAt, createdBy, updatedBy, and updatedAt fields of the entity.
     * It also checks if a userId is provided and throws an exception if it is not.
     * ======================
     */
    @PrePersist
    protected void beforePersist() { //will be called before persisting the entity
        //getting currently logged-in user id
        var userId = RequestContext.getUserId();
        if (userId == null) throw new ApiException("Can't persist a entity without user ID");
        setCreatedBy(userId);
        setUpdatedBy(userId);
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
    /**
     * ======================
     * Updates the entity before it is persisted to the database.
     * This method is annotated with @PreUpdate, which means it will be called by the persistence provider
     * immediately before the entity is updated in the database.
     * The method retrieves the user ID from the RequestContext and throws an ApiException if the user ID is null.
     * It then sets the updatedAt field to the current time and the updatedBy field to the user ID.
     *
     * @throws ApiException if the user ID is null
     *                      ======================
     */
    @PreUpdate
    protected void beforeUpdate() { //will be called before persisting the entity
        var userId = RequestContext.getUserId();
        if (userId == null) throw new ApiException("Can't update a entity without user ID");
        setUpdatedBy(userId);
        setUpdatedAt(LocalDateTime.now());
    }
}