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
    private final CustomerRepository repository;
    private final int accountID;
    private double balance;

    /**
     * Constructs a Customer with given ID, balance, and connection
     *
     * @param accountID unique account ID for customer
     * @param balance the initial balance of the customer
     * @param connection the active database connection
     */
    public Customer(int accountID, double balance, CustomerRepository repository) {
        this.accountID = accountID;
        this.balance = balance;
        this.repository = repository;
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
            boolean result = repository.updateBalance(balance);
            if (result) {
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
            return repository.getBalance(accountID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
