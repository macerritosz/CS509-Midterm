package com.wpi.cs509.entity;

import com.wpi.cs509.repository.AdminRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Administrator class provides administrative operations
 */
public class Administrator {
    private final AdminRepository adminRepository;

    /**
     * Constructs an Administrator instance using the provided AdminRepository.
     *
     * @param adminRepository the repository used to perform admin database ops
     */
    public Administrator(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * Creates a new account in the database.
     *
     * @param login   the account login username
     * @param pin     the account PIN code
     * @param name    the account holder's name
     * @param balance the initial balance
     * @param status  the account status (active / disabled)
     * @throws SQLException if a database error occurs during insertion
     */
    public void pushNewAccount(String login, String pin, String name, String balance, String status) throws SQLException {
        adminRepository.insertAccount(login, pin, name, balance, status);
    }

    /**
     * Deletes an existing account by its account ID.
     *
     * @param accountId the ID of the account to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database error occurs during deletion
     */
    public boolean deleteExistingAccount(String accountId) throws SQLException {
        return adminRepository.deleteAccountById(accountId);
    }

    /**
     * Updates the details of an existing account.
     *
     * @param holder    the updated account holder name
     * @param status    the updated account status
     * @param login     the updated login username
     * @param pin       the updated PIN code
     * @param accountId the ID of the account to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database error occurs during the update
     */
    public boolean updateExistingAccount(String holder, String status, String login, String pin, String accountId) throws SQLException {
        return adminRepository.updateAccount(holder, status, login, pin, accountId);
    }

    /**
     * Retrieves account details for the specified account ID.
     *
     * @param queryType  the type of query ("holder" or "*")
     * @param accountId  the ID of the account
     * @return a ResultSet containing the requested account details
     * @throws SQLException if a database error occurs during the query
     */
    public ResultSet checkAccountExists(String queryType, String accountId) throws SQLException {
        return adminRepository.findAccount(queryType, accountId);
    }
}
