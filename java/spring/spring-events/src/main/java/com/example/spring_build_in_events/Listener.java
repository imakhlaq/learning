package com.example.spring_build_in_events;

import org.springframework.context.event.*;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    @EventListener
    public void contextRefreshed(ContextRefreshedEvent event) {
        System.out.println("Context Refreshed Event");
    }

    @EventListener
    public void contextClosed(ContextClosedEvent event) {

        System.out.println("Context Closed Event");
    }

    @EventListener
    public void contextStarted(ContextStartedEvent event) {

        System.out.println("Context Started Event");
    }

    @EventListener
    public void contextStopped(ContextStoppedEvent event) {
        System.out.println("Context Stopped Event");
    }
}