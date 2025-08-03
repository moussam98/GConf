package com.ensao.gi4.projection;

import java.time.LocalDate;
import java.util.Set;

public interface CallForPapersProjection {
    Long getId();
    LocalDate getStartDate();
    LocalDate getEndDate();
    Set<String> getTopics();
    String getGuidelines();

}
