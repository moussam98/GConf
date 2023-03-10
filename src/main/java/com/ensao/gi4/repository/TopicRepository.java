package com.ensao.gi4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
