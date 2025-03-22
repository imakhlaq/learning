package com.example.spring_events.consumer;

import com.example.spring_events.events.SoloLevelingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChupaListnere {

    @EventListener
    public void onApplicationEvent(SoloLevelingEvent event) {
        System.out.println("Chupa is watching solo leveling episode " + event.getEpNo());
    }
}