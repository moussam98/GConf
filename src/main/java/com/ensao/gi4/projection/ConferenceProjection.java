package com.ensao.gi4.projection;


import java.time.Instant;
import java.time.LocalDate;

public interface ConferenceProjection {

    Long getId();
    String getName();
    String getAcronym();
    String getVenue();
    String getCity();
    String getCountry();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getPrimaryArea();
    String getSecondaryArea();
    String getOrganizer();
    String getPhoneNumber();
    String getOtherInfo();
    UserProjection getOwner();
    CallForPapersProjection getCallForPapers();
    Instant getCreatedAt();
    Instant getUpdatedAt();
}
