package com.example;

import com.example.spring_events.publisher.CrunchRollPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync //<- enabling async methods and Listeners
public class SpringEventsApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(SpringEventsApplication.class, args);

        var bean = context.getBean(CrunchRollPublisher.class);

        //firing custom event
        bean.streamingSoloLeveling("10");

        //firing spring build in events
        context.stop();
        context.close();
    }
}