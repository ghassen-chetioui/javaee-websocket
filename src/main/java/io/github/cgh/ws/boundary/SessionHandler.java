package io.github.cgh.ws.boundary;

import io.github.cgh.ws.entity.Job;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import static java.lang.String.format;

@ApplicationScoped
public class SessionHandler {

    private Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    public void register(Session session, Job job) {
        session.getUserProperties().put("jobId", job.id());
        sessions.add(session);
        Logger.getLogger(SessionHandler.class.getSimpleName()).info(format("Session %s registered for job %s completion", session.getId(), job.id()));
    }

    public void unregister(Session session) {
        try {
            session.close();
            sessions.remove(session);
            Logger.getLogger(SessionHandler.class.getSimpleName()).info(format("Session %s unregistered", session.getId()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void notifyJobCompletedAndUnregister(Job job) {
        Logger.getLogger(SessionHandler.class.getSimpleName()).info(format("Notify job %s completion", job.id()));
        sessions.stream().filter(session -> job.id().equals(session.getUserProperties().get("jobId")))
                .findFirst()
                .ifPresent(s -> {
                    notifyJobCompleted(s, job);
                    unregister(s);
                });
    }

    private void notifyJobCompleted(Session session, Job job) {
        try {
            session.getBasicRemote().sendText(format("Job %s is completed", job.id()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
