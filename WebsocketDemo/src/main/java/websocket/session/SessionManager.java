package websocket.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import websocket.ApiOutgoing;
import websocket.EngineIncoming;
import websocket.messages.CreateAccountMsg;
import websocket.messages.FundAccountMsg;
import websocket.messages.TransferFundMsg;
import websocket.server.ISessionListener;
import websocket.server.ISessionManager;

import java.util.concurrent.*;

public class SessionManager implements ISessionManager {
    private static Logger logger = LogManager.getLogger(SessionManager.class);

    private ExecutorService executor = Executors.newFixedThreadPool(10);
    public static final int PUBLISH_TIME_OUT = 1000;

    private Session session;
    private EngineIncoming engineIncoming;

    public SessionManager(EngineIncoming engineIncoming) {
        this.engineIncoming = engineIncoming;
        engineIncoming.setOutgoing(new ApiOutgoing(this));
    }

    public void sendCreateAccountMsg(CreateAccountMsg msg) {
        engineIncoming.createAccount(msg.getUser(), msg.getCurrency());
    }

    public void sendFundAccountMsg(FundAccountMsg msg) {
        engineIncoming.fundAccount(msg.getAccountId(), msg.getTransCurrency(), msg.getAmount(), msg.getDate());
    }

    public void sendTransferFundMsg(TransferFundMsg msg) {
        engineIncoming.transferFund(msg.getFromAccountId(), msg.getToAccountId(), msg.getTransCurrency(), msg.getAmount(), msg.getDate());
    }

    public void sendListAllAccountsMsg() {
        engineIncoming.listAllAccounts();
    }

    public void sendListAllFundRecordsMsg() {
        engineIncoming.listAllFundRecords();
    }

    public void sendListAllTransferRecordsMsg() {
        engineIncoming.listAllTransferRecords();
    }

    public void sendMessageToSession(String message) {
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

    public void setSession(Session session) {
        this.session = session;
    }

    public ISessionListener getSessionListener() {
        return new SessionListener(this);
    }
}
