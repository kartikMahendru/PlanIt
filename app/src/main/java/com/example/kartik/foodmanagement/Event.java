package com.example.kartik.foodmanagement;

public class Event {
    private String eventName;
    private String imageLink;
    private String eventLink;
    private long count;

    public Event(){

    }

    public Event(String eventName, String imageLink, long count, String eventLink) {
        this.eventName = eventName;
        this.imageLink = imageLink;
        this.count = count;
        this.eventLink = eventLink;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setImageLink(String imageLink) {
        imageLink = imageLink;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getEventName() {
        return eventName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public long getCount() {
        return count;
    }

    public String getEventLink() { return eventLink; }

    public void setEventLink(String eventLink) { this.eventLink = eventLink; }
}