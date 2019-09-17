package io.github.cgh.ws;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

@ServerEndpoint("/job-notifications/{job-id}")
public class WebSocketEndpoint {

    @Inject
    SessionHandler sessionHandler;

    @OnOpen
    public void onOpen(Session session, @PathParam("job-id") String jobId) {
        sessionHandler.register(session, new JobId(jobId));
    }

    @OnClose
    public void onClose(Session session) {
        sessionHandler.unregister(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        Logger.getLogger(WebSocketEndpoint.class.getSimpleName()).severe(throwable.getLocalizedMessage());
        sessionHandler.unregister(session);
    }

}
