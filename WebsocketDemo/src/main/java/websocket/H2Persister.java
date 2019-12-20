package websocket;

import com.etale.otc.Transaction.Status;
import com.etale.otc.messages.RFQMsgOutToOriginator;
import com.etale.otc.messages.RFQResponseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class H2Persister implements PersistenceLayer {

    private static Logger logger = LogManager.getLogger(OTCEngineIncoming.class);
    //        private static final String url = "jdbc:h2:tcp://localhost/~/otctest";
    private static final String url = "jdbc:h2:tcp://localhost:8092/~/otcH2Db/otcdb";
    private static final String user = "sa";
    private static final String password = "";
    private static final String friendResource = "userFriends.properties";
    private Connection connection;

    public H2Persister() {
        getConnection();
        createTables();
        assignFriendTable();
    }

    private void getConnection() {
        try {
            Server.createTcpServer(new String[]{"-tcpPort", "8092", "-tcpAllowOthers"}).start();
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(url, user, password);
            logger.info("Connection Established: " + connection.getMetaData().getDatabaseProductName() + "/" + connection.getCatalog());

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createTables() {
        String createFRIENDS = "CREATE TABLE IF NOT EXISTS FRIENDS (" +
                "user VARCHAR(128), " +
                "friend VARCHAR(128))";
        String createGroups = "CREATE TABLE IF NOT EXISTS GROUPS (" +
                "owner VARCHAR(128), " +
                "groupName VARCHAR(128), " +
                "member VARCHAR(128))";
        String createTransactions = "CREATE TABLE IF NOT EXISTS TRANSACTIONS (" +
                "rfqId VARCHAR(128) NOT NULL, " +
                "originator VARCHAR(128) NOT NULL, " +
                "symbol VARCHAR(8), " +
                "qty FLOAT, " +
                "createdTime BIGINT, " +
                "expiryTime BIGINT, " +
                "status VARCHAR(128), " +
                "acceptedResponseId VARCHAR(128), " +
                "side VARCHAR(5), " +
                "PRIMARY KEY (rfqId) )";
        String createDestinations = "CREATE TABLE IF NOT EXISTS DESTINATIONS (" +
                "rfqId VARCHAR(128) NOT NULL, " +
                "user VARCHAR(128), " +
                "isPublic BIT)";
        String createResponses = "CREATE TABLE IF NOT EXISTS RESPONSES (" +
                "responseId VARCHAR(128) NOT NULL, " +
                "rfqId VARCHAR(128), " +
                "responder VARCHAR(128), " +
                "originator VARCHAR(128), " +
                "offer FLOAT, " +
                "bid FLOAT, " +
                "expiryTime BIGINT, " +
                "PRIMARY KEY (responseId) )";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createFRIENDS);
            statement.executeUpdate(createGroups);
            statement.executeUpdate(createTransactions);
            statement.executeUpdate(createDestinations);
            statement.executeUpdate(createResponses);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void clearData() {
        String insertStr = "DROP TABLE FRIENDS;" +
                "DROP TABLE IF EXISTS SOCIETIES;" +
                "DROP TABLE DESTINATIONS;" +
                "DROP TABLE GROUPS;" +
                "DROP TABLE RESPONSES;" +
                "DROP TABLE TRANSACTIONS;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in dropTables: " + e.getMessage(), e);
        }
    }

    @Override
    public void assignFriendTable() {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String insertStr = "INSERT INTO FRIENDS (user, friend)" +
                "SELECT ?, ?" +
                "WHERE NOT EXISTS (" +
                "    SELECT * FROM FRIENDS WHERE user=? AND friend=?" +
                ") LIMIT 1;";

        try (InputStream resourceStream = loader.getResourceAsStream(friendResource)) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertStr);
            props.load(resourceStream);
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String user = (String) e.nextElement();
                String value = props.getProperty(user);
                String[] friends = value.split(",");
                for (String friend : friends) {
                    preparedStatement.setString(1, user);
                    preparedStatement.setString(2, friend);
                    preparedStatement.setString(3, user);
                    preparedStatement.setString(4, friend);
                    preparedStatement.execute();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createGroup(String user, String groupName) {
        //do nothing
    }

    @Override
    public void deleteGroup(String user, String groupName) {
        String insertStr = "DELETE FROM GROUPS WHERE groupName=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, groupName);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in deleteGroup: " + e.getMessage(), e);
        }
    }

    @Override
    public void addToGroup(String user, String groupName, String newMember) {
        String insertStr = "INSERT INTO GROUPS (owner, groupName, member)" +
                "SELECT ?, ?, ?" +
                "WHERE NOT EXISTS (" +
                "    SELECT * FROM GROUPS WHERE owner=? AND groupName=? AND member=?" +
                ") LIMIT 1;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, groupName);
            preparedStatement.setString(3, newMember);
            preparedStatement.setString(4, user);
            preparedStatement.setString(5, groupName);
            preparedStatement.setString(6, newMember);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in addToGroup: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeFromGroup(String user, String groupName, String member) {
        String insertStr = "DELETE FROM GROUPS WHERE owner=? AND groupName=? AND member=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, groupName);
            preparedStatement.setString(3, member);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in removeFromGroup: " + e.getMessage(), e);
        }

    }

    @Override
    public List<String> getFriendsForUser(String user) {
        String insertStr = "SELECT friend FROM FRIENDS WHERE user=? OR user='ALL'";
        List<String> members = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                members.add(resultSet.getString("friend"));
            }
        } catch (Exception e) {
            logger.error("Error in getFriendsForUser: " + e.getMessage(), e);
        }
        return members;
    }

    @Override
    public List<Group> getGroupsForUser(String user) {
        String insertStr = "SELECT groupName, member FROM GROUPS WHERE owner=? ";
        List<Group> groups = new ArrayList<>();
        Group curGroup = new Group("", "");

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String group = resultSet.getString("groupName");
                String member = resultSet.getString("member");
                if (!group.equals(curGroup.getGroupName())) {
                    if (!curGroup.getGroupName().equals("")) {
                        groups.add(curGroup);
                    }
                    curGroup = new Group(group, user);
                }
                curGroup.addMember(member);
            }
            if (!curGroup.getGroupName().equals("")) {
                groups.add(curGroup);
            }
        } catch (Exception e) {
            logger.error("Error in getGroupsForUser: " + e.getMessage(), e);
        }
        return groups;
    }

    @Override
    public List<String> getGroupNamesForUser(String user) {
        String insertStr = "SELECT DISTINCT groupName FROM GROUPS WHERE owner=? ";
        List<String> groups = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                groups.add(resultSet.getString("groupName"));
            }
        } catch (Exception e) {
            logger.error("Error in getGroupNamesForUser: " + e.getMessage(), e);
        }
        return groups;
    }

    @Override
    public Group getGroup(String user, String groupname) {
        String insertStr = "SELECT groupName, member FROM GROUPS WHERE owner=? AND groupName=?";
        Group curGroup = new Group(groupname, user);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, groupname);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String member = resultSet.getString("member");
                curGroup.addMember(member);
            }
        } catch (Exception e) {
            logger.error("Error in getGroup: " + e.getMessage(), e);
        }
        return curGroup;
    }

    private void storeDestinations(Transaction transaction, String rfqId, boolean isPublic) {
        String insertStr2 = "INSERT INTO DESTINATIONS (rfqId, user, isPublic) " +
                "SELECT ?, ?, ?" +
                "WHERE NOT EXISTS (" +
                "    SELECT * FROM DESTINATIONS WHERE rfqId=? AND user=? AND isPublic=?" +
                ") LIMIT 1;";
        List<String> destinations = isPublic ? transaction.getPublicDestinations() : transaction.getPrivateDestinations();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr2)) {
            if (destinations != null) {
                for (String user : destinations) {
                    preparedStatement.setString(1, rfqId);
                    preparedStatement.setString(2, user);
                    preparedStatement.setBoolean(3, isPublic);
                    preparedStatement.setString(4, rfqId);
                    preparedStatement.setString(5, user);
                    preparedStatement.setBoolean(6, isPublic);
                    preparedStatement.execute();
                }
            }
        } catch (Exception e) {
            logger.error("Error in storeDestinations: " + e.getMessage(), e);
        }
    }

    @Override
    public void storeTransaction(String originator, Transaction transaction) {
        String insertStr = "INSERT INTO TRANSACTIONS (rfqId, originator, symbol, qty, createdTime, expiryTime, status, acceptedResponseId, side) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, transaction.getRfqId());
            preparedStatement.setString(2, originator);
            preparedStatement.setString(3, transaction.getSymbol());
            preparedStatement.setDouble(4, transaction.getQty());
            preparedStatement.setLong(5, transaction.getCreatedTime());
            preparedStatement.setLong(6, transaction.getExpiryTime());
            preparedStatement.setString(7, transaction.getStatus().toString());
            preparedStatement.setString(8, transaction.getAcceptedResponseId());
            preparedStatement.setString(9, String.valueOf(transaction.getSide()));
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in storeTransaction TRANSACTIONS: " + e.getMessage(), e);
        }

        storeDestinations(transaction, transaction.getRfqId(), true);
        storeDestinations(transaction, transaction.getRfqId(), false);
    }

    @Override
    public void changeTransactionStatus(String originator, String rfqId, Status status) {
        String insertStr = "UPDATE TRANSACTIONS SET status=? " +
                "WHERE rfqID=? AND originator=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, String.valueOf(status));
            preparedStatement.setString(2, rfqId);
            preparedStatement.setString(3, originator);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in changeTransactionStatus: " + e.getMessage(), e);
        }
    }

    @Override
    public void setAcceptanceIdNside(String originator, String rfqId, String responseId, Transaction.Side side) {
        String insertStr = "UPDATE TRANSACTIONS SET acceptedResponseId=?,side=? " +
                "WHERE rfqID=? AND originator=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, responseId);
            preparedStatement.setString(2, String.valueOf(side));
            preparedStatement.setString(3, rfqId);
            preparedStatement.setString(4, originator);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in setAcceptanceIdNside: " + e.getMessage(), e);
        }
    }

    @Override
    public RFQResponseMessage getAcceptedResponse(String responseId) {
        String insertStr = "SELECT * FROM RESPONSES WHERE responseId=?;";
        RFQResponseMessage rfqResponseMessage = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, responseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rfqResponseMessage = new RFQResponseMessage(resultSet.getString("responseId"), resultSet.getString("responder"),
                        resultSet.getString("rfqId"), resultSet.getString("originator"),
                        resultSet.getDouble("offer"), resultSet.getDouble("bid"), resultSet.getLong("expiryTime"));
            }
        } catch (Exception e) {
            logger.error("Error in getResponseId: " + e.getMessage(), e);
        }
        return rfqResponseMessage;
    }

    @Override
    public void addTransactionResponse(RFQResponseMessage rfqResponseMessage) {
        String insertStr = "INSERT INTO RESPONSES (rfqId, responseID, responder, originator, offer, bid, expiryTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, rfqResponseMessage.getRfqId());
            preparedStatement.setString(2, rfqResponseMessage.getResponseId());
            preparedStatement.setString(3, rfqResponseMessage.getResponder());
            preparedStatement.setString(4, rfqResponseMessage.getOriginator());
            preparedStatement.setDouble(5, rfqResponseMessage.getOffer());
            preparedStatement.setDouble(6, rfqResponseMessage.getBid());
            preparedStatement.setLong(7, rfqResponseMessage.getExpiryTime());
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in addTransactionResponse: responseId is " + rfqResponseMessage.getResponseId() + ", " + e.getMessage(), e);
        }

    }

    @Override
    public Transaction getTransaction(String rfqId) {
        Transaction transaction = null;

        String insertStr1 = "SELECT * FROM TRANSACTIONS WHERE rfqId=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr1)) {
            preparedStatement.setString(1, rfqId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String originator = resultSet.getString("originator");
                String symbol = resultSet.getString("symbol");
                double qty = resultSet.getDouble("qty");
                long createdTime = resultSet.getLong("createdTime");
                long expiryTime = resultSet.getLong("expiryTime");
                String status = resultSet.getString("status");
                String acceptedResponseId = resultSet.getString("acceptedResponseId");
                String side = resultSet.getString("side");
                transaction = new Transaction(rfqId, originator, symbol, qty, createdTime, expiryTime, new ArrayList<>(), new ArrayList<>());
                transaction.setStatus(Status.valueOf(status));
                transaction.setAcceptedResponseId(acceptedResponseId);
                transaction.setSide(Transaction.Side.valueOf(side));
            }
        } catch (Exception e) {
            logger.error("Error in getTransaction: " + e.getMessage(), e);
        }

        if (transaction != null) {
            setDestinations(transaction.getPublicDestinations(), true, rfqId);
            setDestinations(transaction.getPrivateDestinations(), false, rfqId);
            setResponses(transaction, rfqId);
        }
        return transaction;
    }

    private void setDestinations(List<String> destinations, boolean isPublic, String rfqId) {
        String insertStr = "SELECT user FROM DESTINATIONS WHERE rfqId=? AND isPublic=? ;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, rfqId);
            preparedStatement.setBoolean(2, isPublic);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                destinations.add(resultSet.getString("user"));
            }
        } catch (Exception e) {
            logger.error("Error in setDestinations: " + e.getMessage(), e);
        }
    }

    private void setResponses(Transaction transaction, String rfqId) {
        String insertStr = "SELECT * FROM RESPONSES WHERE rfqId=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, rfqId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                transaction.addResponse(new RFQResponseMessage(resultSet.getString("responseId"), resultSet.getString("responder"),
                        resultSet.getString("rfqId"), resultSet.getString("originator"),
                        resultSet.getDouble("offer"), resultSet.getDouble("bid"), resultSet.getLong("expiryTime")));
            }
        } catch (Exception e) {
            logger.error("Error in setResponses: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RFQMsgOutToOriginator> getRFQsFrom(String user) {
        List<RFQMsgOutToOriginator> RFQMsgOutToOriginators = new ArrayList<>();

        String insertStr = "SELECT rfqId FROM TRANSACTIONS WHERE originator=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Transaction t = getTransaction(resultSet.getString("rfqId"));
                RFQMsgOutToOriginators.add(new RFQMsgOutToOriginator(t.getRfqId(), t.getOriginator(), t.getSymbol(), t.getQty(), t.getCreatedTime(), t.getExpiryTime(),
                        t.getPublicDestinations(), t.getPrivateDestinations(), t.getStatus(), t.getResponses(), t.getAcceptedResponseId(), t.getSide()));
            }
        } catch (Exception e) {
            logger.error("Error in getRFQsFrom: " + e.getMessage(), e);
        }
        return RFQMsgOutToOriginators;
    }

    @Override
    public List<RFQMsgOutToOriginator> getRFQsTo(String user) {
        List<RFQMsgOutToOriginator> rfqsToMe = new ArrayList<>();

        String insertStr = "SELECT DISTINCT rfqId FROM DESTINATIONS WHERE user=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String rfqId = resultSet.getString("rfqId");
                Transaction t = getTransaction(rfqId);
                RFQMsgOutToOriginator curRfq = new RFQMsgOutToOriginator(t.getRfqId(), t.getOriginator(),
                        t.getSymbol(), t.getQty(), t.getCreatedTime(), t.getExpiryTime(), t.getPublicDestinations(),
                        null, t.getStatus(), null, t.getAcceptedResponseId(), t.getSide());
                List<RFQResponseMessage> responseList = new ArrayList<>();
                for (RFQResponseMessage response : t.getResponses()) {
                    if (response.getResponder().equals(user)) {
                        responseList.add(response);
                    }
                }
                curRfq.setResponses(responseList);
                rfqsToMe.add(curRfq);
            }
        } catch (Exception e) {
            logger.error("Error in getRFQsFrom: " + e.getMessage(), e);
        }
        return rfqsToMe;
    }
}