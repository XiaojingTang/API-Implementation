package websocket.messages;

import websocket.objects.TransferRecord;

import java.util.List;

public class AllTransferRecordsMsg extends BaseMessage {
    public static final String TYPE = "ALL_TRANSFER_RECORDS";

    private List<TransferRecord> transferRecords;

    public AllTransferRecordsMsg() {
        super(TYPE);
    }

    public AllTransferRecordsMsg(List<TransferRecord> transferRecords) {
        this();
        this.transferRecords = transferRecords;
    }

    public List<TransferRecord> getTransferRecords() {
        return transferRecords;
    }
}
