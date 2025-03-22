package com.example.spring_async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncExample {

    @Autowired
    AsyncFromDiffClass asyncFromDiffClass;

    @Async
    public void process() throws InterruptedException {
        System.out.println("Processing async event");
        asyncFromDiffClass.earlyWarning();
        asyncFromDiffClass.disaster();
    }
}

/**
 * Async methods needs to be in different class.
 * Because a proxy will be created of the class.
 */
@Component
class AsyncFromDiffClass {

    @Async
    public void earlyWarning() throws InterruptedException {
        System.out.println("Warning early warning");

        Thread.sleep(2000);
        System.out.println("Done early warning");
    }

    @Async
    public void disaster() {
        System.out.println("Disaster early warning");

    }
}