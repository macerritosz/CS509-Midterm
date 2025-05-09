package com.wpi.cs509.repository;

import com.google.inject.Inject;
import com.wpi.cs509.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CustomerRepository provides database access methods for customer-related operations
 */
public class CustomerRepository {
    private final Connection connection;

    /**
     * Constructs a CustomerRepository with an injected DatabaseConnection
     *
     * @param databaseConnection the database connection provider class
     * @throws SQLException if obtaining the connection fails
     */
    @Inject
    public CustomerRepository(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnection();
    }

    /**
     * Updates the balance of the account with the specified account ID.
     *
     * @param accountID the account ID to update
     * @param balance   the new balance value
     * @return  true if the update was successful,  false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateBalance(int accountID, double balance) throws SQLException {
        String updateBalanceQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceQuery)) {
            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, accountID);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        }
    }

    /**
     * Retrieves the balance of the account with the specified account ID.
     *
     * @param accountID the account ID to query
     * @return the account balance, or  0.0 if noy found
     * @throws SQLException if a database error occurs
     */
    public double getBalance(int accountID) throws SQLException {
        String balanceQuery = "SELECT balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(balanceQuery)) {
            preparedStatement.setInt(1, accountID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
            return 0.0;
        }
    }
}
