package io.github.cgh.ws;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Path("jobs")
@ApplicationScoped
public class JobsResource {

    @Resource
    private ManagedExecutorService managedExecutorService;

    @Inject
    private SessionHandler sessionHandler;

    @POST
    public Response post() {
        JobId jobId = new JobId(UUID.randomUUID().toString());
        CompletableFuture.runAsync(() -> {
            try {
                Logger.getLogger(JobsResource.class.getSimpleName()).info("Doing some slow treatment...");
                TimeUnit.SECONDS.wait(3);
                Logger.getLogger(JobsResource.class.getSimpleName()).info("Job terminated...");
            } catch (InterruptedException e) {
                Logger.getLogger(JobsResource.class.getSimpleName()).severe(e.getLocalizedMessage());
            }
        }, managedExecutorService).thenAccept(nothing -> sessionHandler.notifyJobCompleted(jobId));
        return Response.accepted(jobId.value()).build();
    }

}
