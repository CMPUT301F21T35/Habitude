package com.cmput301f21t35.habitude;

import java.sql.Time;
import java.util.Date;

/*
Class for the habit event

TO DO:
may need unique id for event, id is included as a variable just in case
have to find a way for habit to store events
 */
public class Event {
    private String eventName;
    private String eventComment;
    private String eventDate;
    private String eventTime;
    private String geolocation;
    // private String image; for image implementation later

    Event() {}

    Event(String eventName, String eventComment, String eventDate, String eventTime) {
        this.eventName = eventName;
        this.eventComment = eventComment;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    Event(String eventName, String eventComment, String eventDate, String eventTime, String geolocation) {
        this.eventName = eventName;
        this.eventComment = eventComment;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.geolocation = geolocation;
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

}