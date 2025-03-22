package com.example.spring_events.consumer;

import com.example.spring_events.events.SoloLevelingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ChupaListnere {

    @Async
    @EventListener
    public void onApplicationEvent(SoloLevelingEvent event) {
        System.out.println("Chupa is watching solo leveling episode " + event.getEpNo());
    }
}