package com.ensao.gi4.service;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.projection.CallForPapersProjection;
import com.ensao.gi4.projection.ConferenceProjection;
import com.ensao.gi4.projection.UserProjection;

import java.time.Instant;
import java.time.LocalDate;

record ConferenceProjectionImpl(Conference conference) implements ConferenceProjection {

    @Override
    public Long getId() {
        return conference.getId();
    }

    @Override
    public String getName() {
        return conference.getName();
    }

    @Override
    public String getAcronym() {
        return conference.getAcronym();
    }

    @Override
    public String getVenue() {
        return conference.getVenue();
    }

    @Override
    public String getCity() {
        return conference.getCity();
    }

    @Override
    public String getCountry() {
        return conference.getCountry();
    }

    @Override
    public LocalDate getStartDate() {
        return conference.getStartDate();
    }

    @Override
    public LocalDate getEndDate() {
        return conference.getEndDate();
    }

    @Override
    public String getPrimaryArea() {
        return conference.getPrimaryArea();
    }

    @Override
    public String getSecondaryArea() {
        return conference.getSecondaryArea();
    }

    @Override
    public String getOrganizer() {
        return conference.getOrganizer();
    }

    @Override
    public String getPhoneNumber() {
        return conference.getPhoneNumber();
    }

    @Override
    public String getOtherInfo() {
        return conference.getOtherInfo();
    }

    @Override
    public UserProjection getOwner() {
        return new UserProjectionImpl(conference.getOwner());
    }

    @Override
    public CallForPapersProjection getCallForPapers() {
        return new CallForPapersProjectionImpl(conference.getCallForPapers());
    }

    @Override
    public Instant getCreatedAt() {
        return conference.getCreatedAt();
    }

    @Override
    public Instant getUpdatedAt() {
        return conference.getUpdatedAt();
    }
}
