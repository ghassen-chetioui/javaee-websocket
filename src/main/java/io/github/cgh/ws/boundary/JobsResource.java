package io.github.cgh.ws.boundary;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("jobs")
public class JobsResource {

    @Inject
    JobProcessor jobProcessor;

    @POST
    public Response post() {
        String jobId = UUID.randomUUID().toString();
        jobProcessor.process(jobId);
        return Response.accepted(jobId).build();
    }

}
