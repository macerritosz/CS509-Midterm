package com.wpi.cs509.service.CustomerService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Customer class to handle customer operations and state
 * including updating table balance, and fetching updated values
 */
public class Customer {
    private final Connection connection;
    private final int accountID;
    private double balance;

    /**
     * Constructs a Customer with given ID, balance, and connection
     *
     * @param accountID unique account ID for customer
     * @param balance the initial balance of the customer
     * @param connection the active database connection
     */
    public Customer(int accountID, double balance, Connection connection) {
        this.accountID = accountID;
        this.balance = balance;
        this.connection = connection;
    }

    /**
     * Getter for accountID
     *
     * @return integer of Customer Account id
     */
    public int getAccountID() {
        return accountID;
    }

    /**
     * Getter for balance
     *
     * @return Customer balance value
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Setter for balance
     *
     * @param balance new balance to set Customer balance to
     */
    public void setBalance (double balance) {
        this.balance = balance;
    }

    /**
     * A function to handle updating balance for the logged-in customer's account entry in database
     *
     * @param balance a double to update table balance column
     * @return a boolean to indicate if update executed
     */
    public boolean updateBalance(double balance) {
        String updateBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateBalance);
            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, accountID);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                setBalance(balance);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * A function to fetch latest logged-in customer's balance
     *
     * @return a double corresponding to current balance in customer's entry in accounts table
     */
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
