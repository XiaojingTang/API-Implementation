package websocket.session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;
import websocket.server.ISessionListener;

import java.io.IOException;

public class SessionListener implements ISessionListener {
    private static Logger logger = LogManager.getLogger(SessionListener.class);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SessionManager sessionManager;
    private Session session;

    public SessionListener(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void onConnect(Session sess) {
        this.session = sess;
        sessionManager.setSession(session);
        logger.info("Connected: " + sess);
        try {
            session.getRemote().sendString("WebSocket API connected.");
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
                    case CreateAccountMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        CreateAccountMsg createAccountMsg = gson.fromJson(rawMsg, CreateAccountMsg.class);
                        processCreateAccountMsg(createAccountMsg);
                        break;

                    case FundAccountMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        FundAccountMsg fundAccountMsg = gson.fromJson(rawMsg, FundAccountMsg.class);
                        processFundAccountMsg(fundAccountMsg);
                        break;

                    case TransferFundMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        TransferFundMsg transferFundMsg = gson.fromJson(rawMsg, TransferFundMsg.class);
                        processTransferFundMsg(transferFundMsg);
                        break;

                    case ListAllAccountsMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        processListAllAccountsMsg();
                        break;

                    case ListAllFundRecordsMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        processListAllFundRecordsMsg();
                        break;

                    case ListAllTransferRecordsMsg.TYPE:
                        logger.info("Message received: " + rawMsg);
                        processListAllTransferRecordsMsg();
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
        NotificationMsg notificationMsg = new NotificationMsg(error);
        sessionManager.sendMessageToSession(gson.toJson(notificationMsg));
    }

    private void processCreateAccountMsg(CreateAccountMsg msg) {
        sessionManager.sendCreateAccountMsg(msg);
    }

    private void processFundAccountMsg(FundAccountMsg msg) {
        sessionManager.sendFundAccountMsg(msg);
    }

    private void processTransferFundMsg(TransferFundMsg msg) {
        sessionManager.sendTransferFundMsg(msg);
    }

    private void processListAllAccountsMsg() {
        sessionManager.sendListAllAccountsMsg();
    }

    private void processListAllFundRecordsMsg() {
        sessionManager.sendListAllFundRecordsMsg();
    }

    private void processListAllTransferRecordsMsg() {
        sessionManager.sendListAllTransferRecordsMsg();
    }

    @Override
    public void onClose(int status, String reason) {
        logger.info("Removing session: " + reason);
        sessionManager.setSession(null);
        if (this.session != null) {
            this.session.close();
        }
    }

    @Override
    public void onError(String cause) {
        logger.error("Session error: " + cause);
    }
}