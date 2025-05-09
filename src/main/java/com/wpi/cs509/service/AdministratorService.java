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
     * A function to allow Administrator to Create, Edit, Update, and search accounts
     *
     * @throws SQLException if any functions called encounter a database error
     */
    public int createNewAccount(String login, String pin, String name, String balance, String status) throws SQLException {
        return admin.pushNewAccount(login, pin, name, balance, status);
    }

    public boolean deleteExistingAccount(String accountNum) throws SQLException {
        return admin.deleteExistingAccount(accountNum);
    }

    public boolean updateExistingAccount(String holder, String status, String login, String pin, String accountNum) throws SQLException {
        return admin.updateExistingAccount(holder, status, login, pin, accountNum);
    }

    public ResultSet getAccountDetails(String accountNum) throws SQLException {
        return admin.checkAccountExists("*", accountNum);
    }

    public boolean accountExists(String accountNum) throws SQLException {
        ResultSet rs = admin.checkAccountExists("*", accountNum);
        return rs.next();
    }

    public String getAccountHolder(String accountNum) throws SQLException {
        ResultSet accountMatch = admin.checkAccountExists("holder", accountNum);
        if (accountMatch.next()) {
            return accountMatch.getString("holder");
        }
        return null;  // Account does not exist
    }

    @Override
    public String getType() {
        return "admin";
    }
}
