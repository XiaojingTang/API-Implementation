package websocket.messages;

public class ListAllAccountsMsg extends BaseMessage {
    public static final String TYPE = "LIST_ALL_ACCOUNTS";

    public ListAllAccountsMsg() {
        super(TYPE);
    }
}
