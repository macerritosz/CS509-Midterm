package com.wpi.cs509.repository;

import com.google.inject.Inject;
import com.wpi.cs509.database.DatabaseConnection;

import java.sql.*;

public class AdminRepository {
    private final Connection connection;
    @Inject
    public AdminRepository(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnection();
    }

    public int insertAccount(String login, String pin, String name, String balance, String status) throws SQLException {
        String sql = "INSERT INTO accounts (holder, balance, login, pin, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, balance);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, pin);
            preparedStatement.setString(5, status);
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("Account creation failed");
        }
    }

    public boolean deleteAccountById(String accountId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (PreparedStatement preparedDeleteStatement = connection.prepareStatement(sql)) {
            preparedDeleteStatement.setString(1, accountId);
            return preparedDeleteStatement.executeUpdate() == 1;
        }
    }

    public boolean updateAccount(String holder, String status, String login, String pin, String accountId) throws SQLException {
        String sql = "UPDATE accounts SET holder = ?, status = ?, login = ?, pin = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, holder);
            preparedStatement.setString(2, status);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, pin);
            preparedStatement.setString(5, accountId);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean doesLoginExist(String login) throws SQLException {
        String sql = "SELECT 1 FROM accounts WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        }
    }

    public ResultSet findAccount(String queryType, String accountId) throws SQLException {
        String select = queryType.equalsIgnoreCase("holder") ? "holder" : "*";
        String sql = "SELECT " + select + " FROM accounts WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, accountId);
        return preparedStatement.executeQuery();
    }
}
