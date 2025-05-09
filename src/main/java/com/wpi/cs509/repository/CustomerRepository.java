package com.wpi.cs509.repository;

import com.google.inject.Inject;
import com.wpi.cs509.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository {
    private final Connection connection;

    @Inject
    public CustomerRepository(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnection();
    }

    public boolean updateBalance(int accountID, double balance) throws SQLException {
        String updateBalanceQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceQuery)) {
            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, accountID);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        }
    }

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
