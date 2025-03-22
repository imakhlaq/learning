package com.example.springevents.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

public class SoloLevelingEvent extends ApplicationEvent {

    private String epNo;

    public SoloLevelingEvent(Object source) {
        super(source);
    }

    public SoloLevelingEvent(Object source, String epNo) {
        super(source);
        this.epNo = epNo;
    }

    public String getEpNo() {
        return epNo;
    }
}