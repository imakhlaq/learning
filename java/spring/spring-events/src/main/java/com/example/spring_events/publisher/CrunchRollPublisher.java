package com.example.spring_events.publisher;

import com.example.spring_events.events.SoloLevelingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CrunchRollPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void streamingSoloLeveling(String epNo) {
        System.out.println("CrunchRoll : Streaming Solo Leveling " + epNo);

        publisher.publishEvent(new SoloLevelingEvent(epNo));
    }
}