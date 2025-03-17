import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
    To do:
    Refactor Customer to use DI, Create CustomerService/Action Class to oversee actual funcitons, and only make customer
    account balance and ID stuff
    Actual DB updates will be here
 */

public class Customer {
    private final Connection connection;
    private final int accountID;
    private final double balance;

    public Customer(int accountID, double balance, Connection connection) {
        this.accountID = accountID;
        this.balance = balance;
        this.connection = connection;
    }

    public int getAccountID() {
        return accountID;
    }

    public double getBalance() {
        return balance;
    }

    public boolean updateBalance(double balance) {
        String updateBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateBalance);
            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, accountID);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getDataBaseBalance() {
        String balanceQuery = " SELECT * FROM ACCOUNTS WHERE account_id = ?";
        try {
            PreparedStatement preparedLoginMatch = connection.prepareStatement(balanceQuery);
            preparedLoginMatch.setInt(1, accountID);
            ResultSet balanceResult = preparedLoginMatch.executeQuery();
            if (balanceResult.next()) {
                return balanceResult.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
