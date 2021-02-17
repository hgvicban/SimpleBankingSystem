package banking.controllers;

import java.sql.*;

import banking.UserAccount;
import org.sqlite.SQLiteDataSource;

public class DatabaseManager {

    SQLiteDataSource dataSource;

    public DatabaseManager(String url) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
    }

    public void setupDatabase() {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS card ("
                + " id INTEGER PRIMARY KEY,"
                + " number TEXT NOT NULL,"
                + " pin TEXT NOT NULL,"
                + " balance INTEGER DEFAULT 0"
                + ");";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAccount (String cardNum, String cardPIN) {
        String INSERT_SQL = "INSERT INTO CARD (number, pin) VALUES (?,?)";

        try (Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement(INSERT_SQL)) {
            statement.setString(1, cardNum);
            statement.setString(2, cardPIN);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAccountExist (String cardNum) {
        String SELECT_SQL = "SELECT number, pin FROM CARD WHERE number = '" + cardNum + "';";

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            try (ResultSet cardID = statement.executeQuery(SELECT_SQL)) {
                return (cardID.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAccountExist (String cardNum, String cardPIN) {
        String SELECT_SQL = "SELECT number, pin FROM CARD WHERE number = '"
                + cardNum + "' AND pin = '" + cardPIN + "';";

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            try (ResultSet cardID = statement.executeQuery(SELECT_SQL)) {
                return (cardID.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int checkAccountBalance (UserAccount user) {
        String SELECT_SQL = "SELECT balance FROM CARD WHERE number = '"
                + user.getCardNum() + "' AND pin = '" + user.getCardPIN() + "';";

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            try (ResultSet result = statement.executeQuery(SELECT_SQL)) {
                return result.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int increaseAccountBalance(UserAccount user, int income) {
        String UPDATE_SQL = "UPDATE CARD SET balance = balance + ? WHERE number = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, income);
            statement.setString(2, user.getCardNum());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int transferAmount(UserAccount userAccount, String transferCardNum, int amount) {
        String withdrawSQL = "UPDATE CARD SET balance = balance - ? WHERE number = ?";
        String depositSQL  = "UPDATE CARD SET balance = balance + ? WHERE number = ?";

        int affectedRows = 0;
        try (Connection con = dataSource.getConnection()) {
             try (PreparedStatement withdrawAmount = con.prepareStatement(withdrawSQL);
                 PreparedStatement depositAmount = con.prepareStatement(depositSQL)) {

                withdrawAmount.setInt(1, amount);
                withdrawAmount.setString(2, userAccount.getCardNum());
                affectedRows = withdrawAmount.executeUpdate();

                depositAmount.setInt(1, amount);
                depositAmount.setString(2, transferCardNum);
                affectedRows = depositAmount.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return affectedRows;
    }

    public int closeAccount(UserAccount userAccount) {
        String DELETE_SQL = "DELETE FROM CARD WHERE number = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(DELETE_SQL)) {
            statement.setString(1, userAccount.getCardNum());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
