package dev.andreasgeorgatos.pointofservice.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VerificationTokenDeletion {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);


    public void deleteTokens(String email, String token) {
        Runnable task = () -> {

        };

        scheduler.schedule(task, 5, TimeUnit.MINUTES);
    }
}
