package com.example.springevents.consumer;

import com.example.springevents.events.SoloLevelingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AkhlaqListener implements ApplicationListener<SoloLevelingEvent> {
    @Override
    public void onApplicationEvent(SoloLevelingEvent event) {
        System.out.println("Akhlaq is watching solo leveling episode " + event.getEpNo());
    }
}