package com.ensao.gi4.repository;

import com.ensao.gi4.model.Document;
import com.ensao.gi4.projection.DocumentProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<DocumentProjection> findDocumentMetadataById(Long id);
}
