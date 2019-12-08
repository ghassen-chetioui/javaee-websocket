package io.github.cgh.ws.interfaces;

import io.github.cgh.ws.application.Notifier;
import io.github.cgh.ws.entity.Job;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

@ServerEndpoint("/job-notifications")
public class WebSocketEndpoint {

    @Inject
    Notifier notifier;

    @OnOpen
    public void onOpen(Session session) {
        notifier.register(session);
    }

    @OnClose
    public void onClose(Session session) {
        notifier.unregister(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        Logger.getLogger(WebSocketEndpoint.class.getSimpleName()).severe(throwable.getLocalizedMessage());
        notifier.unregister(session);
    }

}
