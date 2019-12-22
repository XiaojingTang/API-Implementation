package websocket.messages;

public class ListAllFundRecordsMsg extends BaseMessage {
    public static final String TYPE = "LIST_ALL_FUND_RECORDS";

    public ListAllFundRecordsMsg() {
        super(TYPE);
    }
}
