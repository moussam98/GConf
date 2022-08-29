package com.ensao.gi4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensao.gi4.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
