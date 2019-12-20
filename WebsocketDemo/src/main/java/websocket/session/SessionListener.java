package websocket.session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercard.messages.BaseMessage;
import com.mastercard.messages.CreateAccountMsg;
import com.mastercard.messages.LoginMsg;
import com.mastercard.messages.LogoutMsg;
import com.mastercard.websocket.ISessionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class SessionListener implements ISessionListener {
    private static Logger logger = LogManager.getLogger(SessionListener.class);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SessionManager sessionManager;
    private SessionInfo SessionInfo = new SessionInfo();
    private Session session;

    public SessionListener(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void onConnect(Session sess) {
        this.session = sess;
        sessionManager.addSession(session, SessionInfo);
        logger.info("Connected: " + sess);
        try {
            session.getRemote().sendString(gson.toJson(new InfoMsg("Connected on build with B2C2Bot.", null, null)));
        } catch (IOException e) {
            logger.error("Error writing to WS", e);
        }
    }

    @Override
    public void onMessage(String rawMsg) {
        BaseMessage msg = gson.fromJson(rawMsg, BaseMessage.class);

        if (msg.getType() == null) {
            logger.error("Invalid message! " + msg);
            notifyError("Invalid message received:" + msg);
        } else {
            try {
                switch (msg.getType()) {
                    case LoginMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        LoginMsg loginMsg = gson.fromJson(rawMsg, LoginMsg.class);
                        processLoginMessage(loginMsg);
                        break;

                    case LogoutMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        LogoutMsg logoutMsg = gson.fromJson(rawMsg, LogoutMsg.class);
                        processLogoutMessage(logoutMsg);
                        break;

                    case CreateAccountMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        CreateAccountMsg createAccountMsg = gson.fromJson(rawMsg, CreateAccountMsg.class);
                        processCreateAccountMsg(createAccountMsg);
                        break;

                    case DeleteGroupMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        DeleteGroupMsg deleteGroupMsg = gson.fromJson(rawMsg, DeleteGroupMsg.class);
                        processDeleteGroupMsg(deleteGroupMsg);
                        break;

                    case "PING":
                        logger.info("Message received: " + rawMsg);
                        PingPongMsg pingMsg = gson.fromJson(rawMsg, PingPongMsg.class);
                        PingPongMsg pongMsg = new PingPongMsg(false, pingMsg.getTimestamp());
                        processPingPongMsg(pongMsg);
                        break;

                    default:
                        logger.error("Unknown message type: " + msg.getType());
                        notifyError("Unknown message type:" + msg.getType());
                }
            } catch (Exception ex) {
                logger.error(ex);
                notifyError("Error processing " + msg.getType() + " message");
            }
        }
    }

    private void notifyError(String error) {
        NotificationMsg notificationMsg = new NotificationMsg(NotificationMsg.Level.ERROR, NotificationMsg.Action.NOTIFY, error, null);
        sessionManager.sendMessageToSession(session, gson.toJson(notificationMsg));
    }

    private void processLoginMessage(LoginMsg msg) {
        sessionManager.sendLogout(new LogoutMsg(msg.getUsername()), true);
        SessionInfo.setSource(msg.getUsername());
        sessionManager.sendLogin(msg.getUsername());
    }

    private void processLogoutMessage(LogoutMsg msg) {
        sessionManager.sendLogout(msg, false);
    }

    private void processCreateAccountMsg(CreateGroupMsg msg) {
        sessionManager.sendCreateGroupMsg(msg);
    }

    private void processDeleteAccountMsg(DeleteGroupMsg msg) {
        sessionManager.sendDeleteGroupMsg(msg);
    }

    private void processPingPongMsg(PingPongMsg pingPongMsg) {
        sessionManager.sendMessageToSession(this.session, gson.toJson(pingPongMsg, PingPongMsg.class));
    }

    @Override
    public void onClose(int status, String reason) {
        logger.info("Removing session: " + reason);
        sessionManager.removeSession(session);
        if (this.session != null) {
            this.session.close();
        }
    }

    @Override
    public void onError(String cause) {
        logger.error("Session error: " + cause);
    }
}