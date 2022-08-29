package com.ensao.gi4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

}
