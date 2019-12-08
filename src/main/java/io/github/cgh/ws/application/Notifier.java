package io.github.cgh.ws.application;

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
public class Notifier {

    private Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    public void register(Session session) {
        sessions.add(session);
        Logger.getLogger(Notifier.class.getSimpleName()).info(format("Session %s is registered", session.getId()));
    }

    public void unregister(Session session) {
        try {
            if (sessions.remove(session)) {
                session.close();
                Logger.getLogger(Notifier.class.getSimpleName()).info(format("Session %s unregistered", session.getId()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void notifyJobCompleted(Job job) {
        Logger.getLogger(Notifier.class.getSimpleName()).info(format("Notify job %s completion", job.id()));
        sessions.forEach(session -> notifyJobCompleted(session, job));
    }

    private void notifyJobCompleted(Session session, Job job) {
        try {
            session.getBasicRemote().sendText(job.id());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
