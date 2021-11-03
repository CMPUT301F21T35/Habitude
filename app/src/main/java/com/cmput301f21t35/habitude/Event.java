package com.cmput301f21t35.habitude;

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
    private Date date;
    // private String image; for image implementation later

    Event() {}

    Event(String eventName, String eventComment, Date date) {
        this.eventName = eventName;
        this.eventComment = eventComment;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}