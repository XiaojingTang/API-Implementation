package websocket;

import com.etale.otc.messages.RFQMsgOutToOriginator;
import com.etale.otc.messages.RFQResponseMessage;

import java.util.List;

public interface PersistenceLayer {

    void createTables();

    void clearData();

    void assignFriendTable();

    void createGroup(String user, String groupName);

    void deleteGroup(String user, String groupName);

    void addToGroup(String user, String groupName, String newMember);

    void removeFromGroup(String user, String groupName, String member);

    List<String> getFriendsForUser(String user);

    List<Group> getGroupsForUser(String user);

    List<String> getGroupNamesForUser(String user);

    Group getGroup(String user, String groupname);

    void storeTransaction(String originator, Transaction transaction);

    void changeTransactionStatus(String originator, String rfqId, Transaction.Status status);

    void setAcceptanceIdNside(String originator, String rfqId, String responseId, Transaction.Side side);

    RFQResponseMessage getAcceptedResponse(String responseId);

    void addTransactionResponse(RFQResponseMessage rfqResponseMessage);

    Transaction getTransaction(String rfqId);

    List<RFQMsgOutToOriginator> getRFQsFrom(String user);

    List<RFQMsgOutToOriginator> getRFQsTo(String user);
}
