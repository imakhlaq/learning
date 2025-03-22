package com.example;

import com.example.spring_async.AsyncExample;
import com.example.spring_events.publisher.CrunchRollPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync //<- enabling async methods and Listeners
public class SpringEventsApplication {

    public static void main(String[] args) throws InterruptedException {
        var context = SpringApplication.run(SpringEventsApplication.class, args);

        var publisher = context.getBean(CrunchRollPublisher.class);

        //firing custom event
        publisher.streamingSoloLeveling("10");

        //async methods
        var asyncMethod = context.getBean(AsyncExample.class);
        asyncMethod.process();

    }
}