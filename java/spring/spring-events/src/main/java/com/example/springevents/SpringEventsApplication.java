package com.example.springevents;

import com.example.springevents.publisher.CrunchRollPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringEventsApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(SpringEventsApplication.class, args);

        var bean = context.getBean(CrunchRollPublisher.class);
        bean.streamingSoloLeveling("10");
    }
}