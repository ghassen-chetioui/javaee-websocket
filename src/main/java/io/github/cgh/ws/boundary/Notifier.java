package io.github.cgh.ws.boundary;

import io.github.cgh.ws.entity.Job;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static java.lang.String.format;

@ApplicationScoped
class Notifier {

    private Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    void register(Session session, Job job) {
        session.getUserProperties().put("jobId", job.id());
        sessions.add(session);
        Logger.getLogger(Notifier.class.getSimpleName()).info(format("Session %s registered for job %s completion", session.getId(), job.id()));
    }

    void unregister(Session session) {
        try {
            if (sessions.remove(session)) {
                session.close();
                Logger.getLogger(Notifier.class.getSimpleName()).info(format("Session %s unregistered", session.getId()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    void notifyJobCompletedAndUnregister(Job job) {
        Logger.getLogger(Notifier.class.getSimpleName()).info(format("Notify job %s completion", job.id()));
        sessions.stream().filter(session -> job.id().equals(session.getUserProperties().get("jobId")))
                .findFirst()
                .ifPresent(session -> {
                    notifyJobCompleted(session, job);
                    unregister(session);
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
