package com.example.spring_async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class AsyncExample {

    @Autowired
    AsyncFromDiffClass asyncFromDiffClass;

    @Autowired
    AsyncWithCompletableFuture asyncWithCompletableFuture;

    public void process() throws InterruptedException {
        System.out.println("Processing async event");
        asyncFromDiffClass.earlyWarning();
        asyncFromDiffClass.disaster();
    }

    //-----------------Completable Future

    public void processAsyncWithCompletableFuture() throws ExecutionException, InterruptedException {

        //running two async methods in a sync manner
        CompletableFuture<Integer> res = this.asyncWithCompletableFuture.asyncProcessA()
            .thenCompose(result -> asyncWithCompletableFuture.asyncProcessB());

        System.out.println(res.get());
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

// Async method with Completable Future
@Component
class AsyncWithCompletableFuture {

    //----------------- returning value from async method--------------
    @Async("myTaskExecutor")   //------ in which bean it need to run
    public CompletableFuture<String> asyncProcessA() {
        return CompletableFuture.completedFuture("A Task");
    }

    @Async("myTaskExecutor")
    public CompletableFuture<Integer> asyncProcessB() {
        return CompletableFuture.supplyAsync(() -> 10);
    }
}