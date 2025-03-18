import java.sql.*;
import java.util.Scanner;

public class Administrator {
    private final Connection connection;


    public Administrator(Connection connection) {
        this.connection = connection;
    }

    public void pushNewAccount(String login, String pin, String name, String balance, String status) throws SQLException {
        String insertStatement = "insert into accounts (holder, balance, login, pin, status) values(?,?,?,?,?)";

        PreparedStatement preparedUserQuery = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        preparedUserQuery.setString(1, name);
        preparedUserQuery.setString(2, balance);
        preparedUserQuery.setString(3, login);
        preparedUserQuery.setString(4, pin);
        preparedUserQuery.setString(5, status);
        int result = preparedUserQuery.executeUpdate();


        if (result == 1) {
            ResultSet rs = preparedUserQuery.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Account Successfully Created -- Account ID: " + rs.getInt(1));
            }
        } else {
            System.err.println("Account Creation Failed\n");
        }
    }

    public void deleteExistingAccount(String accountNum) throws SQLException {
        String deleteStatement = "DELETE FROM accounts WHERE account_id = ?";
        PreparedStatement preparedDeleteStatement = connection.prepareStatement(deleteStatement);
        preparedDeleteStatement.setString(1, accountNum);
        int result = preparedDeleteStatement.executeUpdate();
        if (result == 1) {
            System.out.println("Account Successfully Deleted -- Account ID: " + accountNum +"\n");
        } else {
            System.out.println("Account Deletion Failed\n");
        }
    }

    public void updateExistingAccount(String updatedHolder, String updatedStatus, String updatedLogin, String updatedPin, String accountNum) throws SQLException {
        /* For simplicity, make sure the input cannot be empty, if so retry */
        String updateAccountInfo = "UPDATE accounts SET holder = ?, status = ?, login = ?, pin = ? WHERE account_id = ?";
        PreparedStatement preparedUpdateStatement = connection.prepareStatement(updateAccountInfo);
        preparedUpdateStatement.setString(1, updatedHolder);
        preparedUpdateStatement.setString(2, updatedStatus);
        preparedUpdateStatement.setString(3, updatedLogin);
        preparedUpdateStatement.setString(4, updatedPin);
        preparedUpdateStatement.setString(5, accountNum);
        int result = preparedUpdateStatement.executeUpdate();
        if (result == 1) {
            System.out.println("Account Successfully Updated For Holder: " + updatedHolder);
        } else {
            System.err.println("Account Updated Failed\n");
        }
    }

    public boolean checkLoginExists(String login) {
        try {
            String loginMatchQuery = " SELECT * FROM accounts WHERE login = ?";
            PreparedStatement preparedLoginMatch = connection.prepareStatement(loginMatchQuery);
            preparedLoginMatch.setString(1, login);
            ResultSet loginMatch = preparedLoginMatch.executeQuery();
            return loginMatch.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet checkAccountExists(String queryType, String account) {
        try {
            String type = queryType.toLowerCase();
            if(!type.equals("holder")){
                type = "*";
            }
            String accMatchQuery = "SELECT " + type + " FROM accounts WHERE account_id = ?";
            PreparedStatement preparedLoginMatch = connection.prepareStatement(accMatchQuery);
            preparedLoginMatch.setString(1, account);
            return preparedLoginMatch.executeQuery();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
