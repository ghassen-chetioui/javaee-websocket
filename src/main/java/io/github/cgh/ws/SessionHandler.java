package io.github.cgh.ws;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@ApplicationScoped
class SessionHandler {

    private Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    void register(Session session, JobId jobId) {
        session.getUserProperties().put("jobId", jobId);
        sessions.add(session);
        Logger.getLogger(SessionHandler.class.getSimpleName()).info(String.format("Session %s registered for job %s completion", session.getId(), jobId.value()));
    }

    void unregister(Session session) {
        sessions.remove(session);
        Logger.getLogger(SessionHandler.class.getSimpleName()).info(String.format("Session %s unregistered", session.getId()));
    }

    void notifyJobCompleted(JobId jobId) {
        Logger.getLogger(SessionHandler.class.getSimpleName()).info(String.format("Notify job %s completion", jobId.value()));
        sessions.stream().filter(session -> jobId.equals(session.getUserProperties().get("jobId")))
                .findFirst()
                .ifPresent(s -> notifyJobCompleted(s, jobId));
    }

    private void notifyJobCompleted(Session session, JobId jobId) {
        try {
            session.getBasicRemote().sendText(String.format("Job %s is completed", jobId.value()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
