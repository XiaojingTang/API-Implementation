package websocket.messages;

public class ListAllTransferRecordsMsg extends BaseMessage {
    public static final String TYPE = "LIST_ALL_TRANSFER_RECORDS";

    public ListAllTransferRecordsMsg() {
        super(TYPE);
    }
}
