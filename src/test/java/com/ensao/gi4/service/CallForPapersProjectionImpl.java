package com.ensao.gi4.service;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.projection.CallForPapersProjection;

import java.time.LocalDate;
import java.util.Set;

record CallForPapersProjectionImpl(CallForPapers callForPapers) implements CallForPapersProjection {
    @Override
    public Long getId() {
        return callForPapers.getId();
    }

    @Override
    public LocalDate getStartDate() {
        return callForPapers.getStartDate();
    }

    @Override
    public LocalDate getEndDate() {
        return callForPapers.getEndDate();
    }

    @Override
    public Set<String> getTopics() {
        return callForPapers.getTopics();
    }

    @Override
    public String getGuidelines() {
        return callForPapers.getGuidelines();
    }
}
