package websocket.messages;

import websocket.objects.FundRecord;

import java.util.List;

public class AllFundRecordsMsg extends BaseMessage {
    public static final String TYPE = "ALL_FUND_RECORDS";

    private List<FundRecord> fundRecords;

    public AllFundRecordsMsg() {
        super(TYPE);
    }

    public AllFundRecordsMsg(List<FundRecord> fundRecords) {
        this();
        this.fundRecords = fundRecords;
    }

    public List<FundRecord> getFundRecords() {
        return fundRecords;
    }
}
