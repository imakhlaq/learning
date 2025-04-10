package com.sharefile.securedoc.repository;

import com.sharefile.securedoc.constant.Constants;
import com.sharefile.securedoc.dto.api.IDocument;
import com.sharefile.securedoc.entity.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.sharefile.securedoc.constant.Constants.*;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    /*
    Working with sql with Jpa at the same time
     */

    @Query(countQuery = SELECT_COUNT_DOCUMENT_QUERY, value = SELECT_DOCUMENTS_QUERY, nativeQuery = true)
    Page<IDocument> findDocument(Pageable pageable);

    @Query(countQuery = SELECT_COUNT_DOCUMENT_BY_NAME_QUERY, value = SELECT_DOCUMENT_BY_NAME_QUERY, nativeQuery = true)
    Page<IDocument> findDocumentByName(@Param("documentName") String documentName, Pageable pageable);

    @Query(value = SELECT_DOCUMENT_QUERY, nativeQuery = true)
    Optional<IDocument> findDocumentByDocumentId(String documentId);

    Optional<DocumentEntity> findByDocumentId(String documentId);
}