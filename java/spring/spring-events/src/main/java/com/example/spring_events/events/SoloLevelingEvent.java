package com.example.spring_events.events;

public class SoloLevelingEvent {

    private String epNo;

    public SoloLevelingEvent(String epNo) {
        this.epNo = epNo;
    }

    public String getEpNo() {
        return epNo;
    }
}