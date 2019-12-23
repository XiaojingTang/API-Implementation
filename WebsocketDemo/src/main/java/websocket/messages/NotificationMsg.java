package websocket.messages;

public class NotificationMsg extends BaseMessage {
    public static final String TYPE = "NOTIFICATION";

    private String message;

    public NotificationMsg() {
        super(TYPE);
    }

    public NotificationMsg(String message) {
        this();
        this.message = message;
    }
}
