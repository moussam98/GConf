package com.ensao.gi4.service.impl;

import com.ensao.gi4.model.Keyword;
import com.ensao.gi4.repository.KeywordRepository;
import com.ensao.gi4.service.api.KeywordService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
record KeywordServiceImpl(KeywordRepository keywordRepository) implements KeywordService {

    @Override
    public void addAll(Set<Keyword> keywords) {
        keywordRepository.saveAll(keywords);
    }
}
