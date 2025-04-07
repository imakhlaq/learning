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
    @Column(name = "id", updatable = false, nullable = false)
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
    @NotNull
    private LocalDateTime updatedAt;

    @PrePersist
    protected void beforePersist() { //will be called before persisting the entity
        var userId = RequestContext.getUserId();
        if (userId == null) throw new ApiException("Can't persist a entity without user ID");
        setCreatedBy(userId);
        setCreatedBy(userId);
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    protected void beforeUpdate() { //will be called before persisting the entity
        var userId = RequestContext.getUserId();
        if (userId == null) throw new ApiException("Can't update a entity without user ID");
        setCreatedBy(userId);
        setUpdatedAt(LocalDateTime.now());
    }
}