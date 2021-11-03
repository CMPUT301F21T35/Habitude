package com.cmput301f21t35.habitude;

/*
Class for the habit event

TO DO:
may need unique id for event, id is included as a variable just in case
have to find a way for habit to store events
 */
public class Event {
    private String eventName;
    private String eventComment;
    // private String image; for image implementation later

    Event() {}

    Event(String eventName, String eventComment) {
        this.eventName = eventName;
        this.eventComment = eventComment;
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
}