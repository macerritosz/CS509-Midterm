package com.wpi.cs509.service.AdministratorService;

import java.sql.*;

/**
 * Administrator class provides administrative operations on the Accounts table,
 * including creating, updating, deleting accounts, and checking account existence.
 */
public class Administrator {
    private final Connection connection;


    /**
     * Constructs an Administrator with the given database connection.
     *
     * @param connection the database connection to be used for executing queries
     */
    public Administrator(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts new row into Accounts table with the following information and prints generated ID
     *
     * @param login user login identifier
     * @param pin a 5 digit PIN passed as a String
     * @param name account Holder's name
     * @param balance initial balance of the account, representing a Double
     * @param status current account status ACTIVE / DISABLED
     * @throws SQLException If database execution or access error occurs
     */
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

    /**
     * Deletes row from Accounts table corresponding to accountNum and prints confirmation message
     *
     * @param accountNum an accounts unique ID as a string
     * @throws SQLException If database execution error occurs
     */
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

    /**
     * Updates table entry in accounts corresponding to the following passed in and prints confirmation message
     *
     * @param updatedHolder string for updated account holder name
     * @param updatedStatus string for ACTIVE / DISABLED
     * @param updatedLogin string for new login for account
     * @param updatedPin string for new 5 digit PIN code
     * @param accountNum  string for unique number of account to edit
     * @throws SQLException If a database access / write error occurs
     */
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

    /**
     * A function to see if unique user login already exists in accounts table
     *
     * @param login a string corresponding to a user's login
     * @return a boolean, true if it does exist in table, false otherwise
     */
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

    /**
     * Checks if account exists in accounts table and returns the result set containing its information
     *
     * @param queryType a string corresponding what values to return in result set [ * - all columns, "holder" - holder column]
     * @param account a string for the unique account ID in table
     * @return a ResultSet object for the found entry or null
     */
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
