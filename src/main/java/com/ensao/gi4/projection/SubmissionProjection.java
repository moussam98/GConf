package com.ensao.gi4.projection;

import java.util.List;
import java.util.Set;

public interface SubmissionProjection {
    Long getId();
    String getTitle();
    String getDescription();
    Set<String> getKeywords();
    DocumentProjection getDocument();
    List<AuthorProjection> getAuthors();
    Boolean getIsEvaluate();
    Boolean getIsValidate();
}
