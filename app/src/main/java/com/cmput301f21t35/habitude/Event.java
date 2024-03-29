package com.cmput301f21t35.habitude;

import java.sql.Time;
import java.util.Date;

/**
 * @author echiu
 * Class for the habit event
 */
public class Event {
    private String eventName;
    private String eventComment;
    private String eventDate;
    private String eventTime;
    private Boolean eventFinished;
    private String eventGeolocation = "null";
    private String eventPhoto = "null";
    // private String image; for image implementation later

    Event() {}

    Event(String eventName, String eventComment, String eventDate, String eventTime, Boolean eventFinished) {
        this.eventName = eventName;
        this.eventComment = eventComment;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventFinished = eventFinished;
    }

    Event(String eventName, String eventComment, String eventDate, String eventTime, Boolean eventFinished, String eventGeolocation, String eventPhoto) {
        this.eventName = eventName;
        this.eventComment = eventComment;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventFinished = eventFinished;
        this.eventGeolocation = eventGeolocation;
        this.eventPhoto = eventPhoto;
    }

// getters and setters

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventComment() {
        return eventComment;
    }

    public void setEventComment(String eventComment) {
        this.eventComment = eventComment;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Boolean getEventFinished() {
        return eventFinished;
    }

    public void setEventFinished(Boolean eventFinished) {
        this.eventFinished = eventFinished;
    }

    public String getEventGeolocation() {
        return eventGeolocation;
    }

    public void setEventGeolocation(String eventGeolocation) {
        this.eventGeolocation = eventGeolocation;
    }

    public String getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(String eventPhoto) {
        this.eventPhoto = eventPhoto;
    }
}