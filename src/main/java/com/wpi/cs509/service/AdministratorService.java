package com.wpi.cs509.service;

import com.wpi.cs509.entity.Administrator;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * AdministratorService provides an interface for administrators to manage
 * accounts, including creating, deleting, updating, and searching for accounts.
 * It implements the IUserService interface.
 */
public class AdministratorService implements IUserService {
    private final Administrator admin;

    /**
     * Constructs an AdministratorService with the provided Administrator object.
     *
     * @param admin the Administrator instance used to perform administrative operations
     */
    public AdministratorService(Administrator admin) {
        this.admin = admin;
    }


    /**
     * Creates a new account with the specified details.
     *
     * @param login    the login username for the account
     * @param pin      the PIN code for the account
     * @param name     the account holder's name
     * @param balance  the initial balance for the account
     * @param status   the account status (e.g., active, inactive)
     * @throws SQLException if a database error occurs while creating the account
     */
    public void createNewAccount(String login, String pin, String name, String balance, String status) throws SQLException {
        admin.pushNewAccount(login, pin, name, balance, status);
    }


    /**
     * Deletes an existing account with the specified account number.
     *
     * @param accountNum the account number of the account to delete
     * @return true if the account was successfully deleted, false otherwise
     * @throws SQLException if a database error occurs while deleting the account
     */
    public boolean deleteExistingAccount(String accountNum) throws SQLException {
        return admin.deleteExistingAccount(accountNum);
    }


    /**
     * Updates an existing account with the provided details.
     *
     * @param holder     the updated account holder name
     * @param status     the updated account status
     * @param login      the updated login username
     * @param pin        the updated PIN code
     * @param accountNum the account number to update
     * @return true if the account was successfully updated, false otherwise
     * @throws SQLException if a database error occurs while updating the account
     */
    public boolean updateExistingAccount(String holder, String status, String login, String pin, String accountNum) throws SQLException {
        return admin.updateExistingAccount(holder, status, login, pin, accountNum);
    }


    /**
     * Retrieves the account with the specified account number.
     *
     * @param accountNum the account number to retrieve details for
     * @return a ResultSet containing the account details, or an empty result if the account does not exist
     * @throws SQLException if a database error occurs while retrieving the account details
     */
    public ResultSet getAccountDetails(String accountNum) throws SQLException {
        return admin.checkAccountExists("*", accountNum);
    }

    /**
     * Checks whether an account with the specified account number exists.
     *
     * @param accountNum the account number to check
     * @return true if the account exists, false otherwise
     * @throws SQLException if a database error occurs while checking the account existence
     */
    public boolean accountExists(String accountNum) throws SQLException {
        ResultSet rs = admin.checkAccountExists("*", accountNum);
        return rs.next();
    }

    /**
     * Retrieves the name of the account holder for the specified account number.
     *
     * @param accountNum the account number to retrieve the holder's name for
     * @return the account holder's name if the account exists, or null if not found
     * @throws SQLException if a database error occurs while retrieving the holder's name
     */
    public String getAccountHolder(String accountNum) throws SQLException {
        ResultSet accountMatch = admin.checkAccountExists("holder", accountNum);
        if (accountMatch.next()) {
            return accountMatch.getString("holder");
        }
        return null;  // Account does not exist
    }

    /**
     * A function to get the service Type
     *
     * @return a string of the word "admin"
     */
    @Override
    public String getType() {
        return "admin";
    }
}
