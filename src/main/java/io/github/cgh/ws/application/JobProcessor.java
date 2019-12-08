package io.github.cgh.ws.application;

import io.github.cgh.ws.entity.Job;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Stateless
public class JobProcessor {

    @Resource
    ManagedExecutorService managedExecutorService;

    @Inject
    Notifier notifier;

    public void process(String jobId) {
        Job job = new Job(jobId);
        CompletableFuture.runAsync(() -> {
            try {
                Logger.getLogger(JobProcessor.class.getSimpleName()).info("Doing some slow treatment...");
                Thread.sleep(3000);
                Logger.getLogger(JobProcessor.class.getSimpleName()).info("Job terminated...");
            } catch (InterruptedException e) {
                Logger.getLogger(JobProcessor.class.getSimpleName()).severe(e.getLocalizedMessage());
            }
        }, managedExecutorService).thenAccept(nothing -> notifier.notifyJobCompleted(job));
    }
}
