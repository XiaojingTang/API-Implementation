package websocket.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import websocket.EngineIncoming;
import websocket.EngineOutgoing;
import websocket.messages.LogoutMsg;
import websocket.server.ISessionListener;
import websocket.server.ISessionManager;

import java.util.Map;
import java.util.concurrent.*;

public class SessionManager implements ISessionManager {
    private static Logger logger = LogManager.getLogger(SessionManager.class);
    private IncrementingIDGenerator idGenerator = new IncrementingIDGenerator();

    private ExecutorService executor = Executors.newFixedThreadPool(10);
    public static final int PUBLISH_TIME_OUT = 1000;

    private ConcurrentHashMap<Session, SessionInfo> sessionMap = new ConcurrentHashMap<>();
    private EngineIncoming engineIncoming;

    public SessionManager(EngineIncoming engineIncoming) {
        this.engineIncoming = engineIncoming;
        idGenerator.initialize(System.currentTimeMillis());
    }

    public void sendLogin(String user) {
        EngineOutgoing engineOutgoing = new EngineOutgoing(this, user);
        engineIncoming.login(engineOutgoing);
    }

    public void sendLogin(EngineOutgoing user) {
        engineIncoming.login(user);
    }

    public void sendLogout(LogoutMsg msg, Boolean forceSignOut) {
        Session session = findSession(msg.getUsername());
        if (session != null) {
            logger.info("Removing session for user: " + msg.getUsername());
            if (forceSignOut) {
                String reason = MsgToStringConverter.convert(new ForceSignoutMsg("You have been signed out because you have logged in at a different location."));
                sendMessageToSession(session, reason);
            }
            removeSession(session);
        }
    }


    public void sendCreateGroupMsg(CreateGroupMsg msg) {
        engineIncoming.createGroup(msg.getUsername(), msg.getGroupName());
    }

    public void sendDeleteGroupMsg(DeleteGroupMsg msg) {
        engineIncoming.deleteGroup(msg.getUsername(), msg.getGroupName());
    }

    public void sendAddToGroupMsg(AddToGroupMsg msg) {
        engineIncoming.addToGroup(msg.getUsername(), msg.getGroupName(), msg.getNewMember());
    }

    public void sendRemoveFromGroupMsg(RemoveFromGroupMsg msg) {
        engineIncoming.removeFromGroup(msg.getUsername(), msg.getGroupName(), msg.getMember());
    }

    private Session findSession(String user) {
        for (Map.Entry<Session, SessionInfo> entry : sessionMap.entrySet()) {
            if (user.equals(entry.getValue().getSource())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void sendMessageToSession(String user, String message) {
        sendMessageToSession(findSession(user), message);
    }

    public void sendMessageToSession(Session session, String message) {
        try {
            executor.submit(() -> {
                Future<Void> publishFuture = null;
                try {
                    publishFuture = session.getRemote().sendStringByFuture(message);
                    // wait for completion (timeout)
                    publishFuture.get(PUBLISH_TIME_OUT, TimeUnit.MILLISECONDS);
                } catch (ExecutionException | InterruptedException e) {
                    logger.error("Unable to send message", e);
                } catch (TimeoutException e) {
                    logger.error("Timeout sending message", e);
                    // timeout
                    if (publishFuture != null) {
                        // cancel the message
                        publishFuture.cancel(true);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Error publishing update", e);
        }
    }

    public void addSession(Session session, SessionInfo info) {
        sessionMap.put(session, info);
    }

    public void removeSession(Session session) {
        engineIncoming.logout(sessionMap.get(session).getSource());
        sessionMap.remove(session);
    }

    public IncrementingIDGenerator getIdGenerator() {
        return idGenerator;
    }

    public ISessionListener getSessionListener() {
        return new SessionListener(this);
    }
}
