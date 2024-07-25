package dev.andreasgeorgatos.pointofservice.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncVerificationCodeDeletion {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void deleteVerificationCode() {
        scheduler.schedule(new DeleteToken(), 5, TimeUnit.MINUTES);
    }
}
