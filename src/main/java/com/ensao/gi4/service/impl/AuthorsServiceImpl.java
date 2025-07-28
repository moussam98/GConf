package com.ensao.gi4.service.impl;

import com.ensao.gi4.model.Author;
import com.ensao.gi4.repository.AuthorRepository;
import com.ensao.gi4.service.api.AuthorsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
record AuthorsServiceImpl(AuthorRepository authorRepository) implements AuthorsService {

    @Override
    public void addAll(List<Author> authors) {
        authorRepository.saveAll(authors);
    }
}
