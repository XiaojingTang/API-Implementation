package websocket.persistence;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;
import websocket.objects.Account;
import websocket.objects.FundRecord;
import websocket.objects.TransferRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2Persister implements Persister {

    private static Logger logger = LogManager.getLogger(H2Persister.class);
    private static final String url = "jdbc:h2:tcp://localhost:8092/~/test";
    private static final String user = "sa";
    private static final String password = "";
    private Connection connection;

    public H2Persister() {
        getConnection();
        createTables();
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
        String createAccount = "CREATE TABLE IF NOT EXISTS ACCOUNT (" +
                "id BIGINT AUTO_INCREMENT," +
                "user VARCHAR(128) NOT NULL, " +
                "currency VARCHAR(128) NOT NULL," +
                "balance FLOAT)";
        String createFundRecord = "CREATE TABLE IF NOT EXISTS FUNDRECORD (" +
                "id BIGINT NOT NULL AUTO_INCREMENT," +
                "accountId BIGINT NOT NULL, " +
                "amount FLOAT, " +
                "bankfee BIGINT, " +
                "conversionRate FLOAT, " +
                "crdhldBillAmt FLOAT, " +
                "date VARCHAR(128), " +
                "transCurrency VARCHAR(128) NOT NULL, " +
                "PRIMARY KEY (id) )";
        String createTransferRecord = "CREATE TABLE IF NOT EXISTS TRANSFERRECORD (" +
                "id BIGINT NOT NULL AUTO_INCREMENT," +
                "fromAccountId BIGINT NOT NULL, " +
                "toAccountId BIGINT NOT NULL, " +
                "amount FLOAT, " +
                "bankfee BIGINT, " +
                "conversionRate FLOAT, " +
                "crdhldBillAmt FLOAT, " +
                "date VARCHAR(128), " +
                "transCurrency VARCHAR(128) NOT NULL, " +
                "PRIMARY KEY (id) )";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createAccount);
            statement.executeUpdate(createFundRecord);
            statement.executeUpdate(createTransferRecord);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void clearData() {
        String insertStr = "DROP TABLE ACCOUNT;" +
                "DROP TABLE FUNDRECORD;" +
                "DROP TABLE TRANSAFERRECORD;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in dropTables: " + e.getMessage(), e);
        }
    }

    @Override
    public void addToAccount(String user, String currency) {
        String insertStr = "INSERT INTO ACCOUNT (user, currency, balance) " +
                "VALUES (?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, currency);
            preparedStatement.setDouble(3, 0.0);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in addToAccount: " + e.getMessage(), e);
        }
    }

    @Override
    public void addToFundRecord(long accountId, String transCurrency, double amount, String date, double conversionRate,
                                double crdhldBillAmt, long bankFee) {
        String insertStr = "INSERT INTO FUNDRECORD (accountId, transCurrency, amount, date, conversionRate, crdhldBillAmt, bankFee)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setLong(1, accountId);
            preparedStatement.setString(2, transCurrency);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, date);
            preparedStatement.setDouble(5, conversionRate);
            preparedStatement.setDouble(6, crdhldBillAmt);
            preparedStatement.setLong(7, bankFee);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in addToFundRecord: " + e.getMessage(), e);
        }
    }

    @Override
    public void addToTransferRecord(long fromAccountId, long toAccountId, String transCurrency, double amount, String date, double conversionRate,
                                    double crdhldBillAmt, long bankFee) {
        String insertStr = "INSERT INTO TRANSFERRECORD (fromAccountId, toAccountId, transCurrency, amount, date, conversionRate, crdhldBillAmt, bankFee)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setLong(1, fromAccountId);
            preparedStatement.setLong(2, toAccountId);
            preparedStatement.setString(3, transCurrency);
            preparedStatement.setDouble(4, amount);
            preparedStatement.setString(5, date);
            preparedStatement.setDouble(6, conversionRate);
            preparedStatement.setDouble(7, crdhldBillAmt);
            preparedStatement.setLong(8, bankFee);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in addToFundRecord: " + e.getMessage(), e);
        }
    }

    @Override
    public Account findById(long accountId) {
        String insertStr = "SELECT * FROM ACCOUNT" +
                "WHILE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setLong(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(resultSet.getLong("id"), resultSet.getString("user"),
                        resultSet.getString("currency"), resultSet.getDouble("balance"));
                return account;
            }
        } catch (Exception e) {
            logger.error("Error in findById: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void setAccountBalance(long accountId, double amount) {
        String insertStr = "UPDATE ACCOUNT SET balance=? " +
                "WHERE accountId=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setLong(2, accountId);
            preparedStatement.execute();
        } catch (Exception e) {
            logger.error("Error in setAccountBalance: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        String insertStr = "SELECT * FROM ACCOUNT;";
        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(resultSet.getLong("id"), resultSet.getString("user"),
                        resultSet.getString("currency"), resultSet.getDouble("balance"));
                accounts.add(account);
            }
        } catch (Exception e) {
            logger.error("Error in getAllAccounts: " + e.getMessage(), e);
        }
        return accounts;
    }

    @Override
    public List<FundRecord> getAllFundRecord() {
        String insertStr = "SELECT * FROM FUNDRECORD;";
        List<FundRecord> fundRecords = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                FundRecord fundRecord = new FundRecord(resultSet.getLong("id"), resultSet.getLong("accountId"),
                        resultSet.getString("transCurrency"), resultSet.getDouble("amount"),
                        resultSet.getString("date"), resultSet.getDouble("conversionRate"),
                        resultSet.getDouble("crdhldBillAmt"), resultSet.getLong("bankFee"));
                fundRecords.add(fundRecord);
            }
        } catch (Exception e) {
            logger.error("Error in getAllFundRecord: " + e.getMessage(), e);
        }
        return fundRecords;
    }

    @Override
    public List<TransferRecord> getAllTransferRecord() {
        String insertStr = "SELECT * FROM TRANSFERRECORD;";
        List<TransferRecord> transferRecords = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TransferRecord transferRecord = new TransferRecord(resultSet.getLong("id"),
                        resultSet.getLong("fromAccountId"), resultSet.getLong("toAccountId"),
                        resultSet.getString("transCurrency"), resultSet.getDouble("amount"),
                        resultSet.getString("date"), resultSet.getDouble("conversionRate"),
                        resultSet.getDouble("crdhldBillAmt"), resultSet.getLong("bankFee"));
                transferRecords.add(transferRecord);
            }
        } catch (Exception e) {
            logger.error("Error in getAllTransferRecord: " + e.getMessage(), e);
        }
        return transferRecords;
    }
}