package com.example.kartik.foodmanagement;

/**
 * Created by kartik on 17/10/19.
 */

public class Event {
    private String eventName;
    private String ImageLink;
    private int count;

    public void Event(){

    }

    public Event(String eventName, String imageLink, int count) {
        this.eventName = eventName;
        ImageLink = imageLink;
        this.count = count;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEventName() {
        return eventName;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public int getCount() {
        return count;
    }
}
