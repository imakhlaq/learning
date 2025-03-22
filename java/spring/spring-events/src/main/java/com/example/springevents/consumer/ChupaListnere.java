package com.example.springevents.consumer;

import com.example.springevents.events.SoloLevelingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ChupaListnere implements ApplicationListener<SoloLevelingEvent> {
    @Override
    public void onApplicationEvent(SoloLevelingEvent event) {
        System.out.println("Chupa is watching solo leveling episode " + event.getEpNo());
    }
}