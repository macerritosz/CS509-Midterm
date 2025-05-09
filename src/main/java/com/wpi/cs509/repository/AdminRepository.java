package com.wpi.cs509.repository;

import com.google.inject.Inject;
import com.wpi.cs509.database.DatabaseConnection;

import java.sql.*;

/**
 * AdminRepository provides database access methods
 * for administrator account operations such as creating, updating, deleting, and querying accounts.
 */
public class AdminRepository {
    private final Connection connection;

    /**
     * Constructs an AdminRepository with an injected DatabaseConnection.
     *
     * @param databaseConnection the database connection provider
     * @throws SQLException if obtaining the connection fails
     */
    @Inject
    public AdminRepository(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnection();
    }

    /**
     * Inserts a new account record into the database.
     *
     * @param login   the login username
     * @param pin     the PIN code
     * @param name    the account holder's name
     * @param balance the initial balance
     * @param status  the account status
     * @return the generated account ID if successful
     * @throws SQLException if the account creation fails
     */
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

    /**
     * Deletes the account with the specified account ID.
     *
     * @param accountId the account ID to delete
     * @return true if the account was successfully deleted, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteAccountById(String accountId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (PreparedStatement preparedDeleteStatement = connection.prepareStatement(sql)) {
            preparedDeleteStatement.setString(1, accountId);
            return preparedDeleteStatement.executeUpdate() == 1;
        }
    }

    /**
     * Updates the details of an existing account.
     *
     * @param holder    the updated account holder name
     * @param status    the updated account status
     * @param login     the updated login username
     * @param pin       the updated PIN code
     * @param accountId the account ID to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Checks whether an account with the specified login exists.
     *
     * @param login the login username to check
     * @return true if the login exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean doesLoginExist(String login) throws SQLException {
        String sql = "SELECT 1 FROM accounts WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Finds an account by account ID and returns the requested fields.
     *
     * @param queryType  the type of field to query (e.g., "holder" or "*")
     * @param accountId  the account ID to search for
     * @return a ResultSet containing the account details
     * @throws SQLException if a database error occurs
     */
    public ResultSet findAccount(String queryType, String accountId) throws SQLException {
        String select = queryType.equalsIgnoreCase("holder") ? "holder" : "*";
        String sql = "SELECT " + select + " FROM accounts WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, accountId);
        return preparedStatement.executeQuery();
    }
}
