package com.wpi.cs509.service.AdministratorService;

import com.wpi.cs509.service.IUserService;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * AdministratorService provides an interface for administrators to manage
 * accounts, including creating, deleting, updating, and searching for accounts.
 * It implements the IUserService interface.
 */
public class AdministratorService implements IUserService {
    private final Administrator admin;
    private final AdminUtils utils;
    private final AdminUI ui;

    /**
     * Constructs an AdministratorService with the provided Administrator object.
     *
     * @param admin the Administrator instance used to perform administrative operations
     */
    public AdministratorService(Administrator admin, AdminUtils utils, AdminUI ui) {
        this.admin = admin;
        this.utils = utils;
        this.ui = ui;
    }

    /**
     * A function to allow Administrator to Create, Edit, Update, and search accounts
     *
     * @throws SQLException if any functions called encounter a database error
     */
    public void showUserActions() throws SQLException {
        ui.showAdminActions();

        String choice = ui.getInput("Enter your choice: ");

        switch (choice) {
            case "1":
                createNewAccount();
                break;
            case "2":
                deleteExistingAccount();
                break;
            case "3":
                updateExistingAccount();
                break;
            case "4":
                searchForAccount();
                break;
            default:
                ui.displayMessage("Exiting...");
                System.exit(0);
                break;
        }
    }
    public boolean checkData (String login, String pin , String balance, String status) throws SQLException {
        return !admin.checkLoginExists(login) && utils.checkBalance(balance) && utils.checkStatus(status) && utils.checkPIN(pin);
    }

    private void createNewAccount() throws SQLException {
        String login = "";
        String pin = "";
        String name = "";
        String balance = "";
        String status = "";

        do {
            ui.displayMessage("Creating new account... ");
            login = ui.getInput("Enter new account Login: ");
            pin = ui.getInput("Enter a 5 digit account PIN: ");
            name = ui.getInput("Enter new account Holder's Name: ");
            balance = ui.getInput("Enter new account Starting Balance: ");
            status = ui.getInput("Enter new account Status: ");
        } while (checkData(login, pin , balance, status));

        try {
            int accID = admin.pushNewAccount(login, pin, name, balance, status);
            ui.displayMessage("Account Successfully Created -- Account ID: " + accID);
        } catch (SQLException e) {
            ui.displayError("Error while creating new account \n");
        }

        ui.showAdminActions();
    }

    private void deleteExistingAccount() throws SQLException {
        String accountNum = "";
        String matchNum = "";

        boolean error;
        do {
            ui.displayMessage("Deleting An account... ");
            accountNum = ui.getInput("Enter account number for account to be deleted: ");
            error = utils.checkAccNumber(accountNum);
        } while (error);
        //check not empty and only numeric
        ResultSet accountMatch = admin.checkAccountExists("holder", accountNum);

        if (accountMatch.next()) {
            String holder = accountMatch.getString("holder");
            matchNum = ui.getInput("You wish to delete the account held by " + holder + ". Please re-enter the account Number: ");
            if(!utils.checkAccNumber(matchNum)) {
                ui.showAdminActions();
            }
            if (accountNum.equals(matchNum)) {

                boolean result = admin.deleteExistingAccount(matchNum);
                if (result) {
                    ui.displayMessage("Account Successfully Deleted -- Account ID: " + accountNum + "\n");
                } else {
                    ui.displayError("Account Deletion Failed\n");
                }
                ui.showAdminActions();
            } else {
                ui.displayError("Numbers Did Not Match, Please pick another action... \n");
                ui.showAdminActions();
            }
        } else {
            ui.displayError("Account Number is Not Assigned \n");
            ui.showAdminActions();
        }
    }

    private void updateExistingAccount() throws SQLException {
        String accountNum = "";
        boolean error;

        do {
            ui.displayMessage("Updating an account...");
            accountNum = ui.getInput("Enter account number for information updating: ");
            error = utils.checkAccNumber(accountNum);
        } while (error);

        ResultSet accountMatch = admin.checkAccountExists("*", accountNum);
        if (!accountMatch.next()) {
            ui.displayError("Account not found");
            ui.showAdminActions();
            return;
        }

        ui.displayMessage("\nAccount Found");

        String updatedHolder = ui.getInput("Updated Holder: ");
        String updatedStatus = ui.getInput("Updated Status (ACTIVE/DISABLED): ");
        String updatedLogin = ui.getInput("Updated Login: ");
        String updatedPin = ui.getInput("Updated 5-digit Pin: ");

        if (!updatedStatus.equalsIgnoreCase("ACTIVE") && !updatedStatus.equalsIgnoreCase("DISABLED")) {
            ui.displayError("Invalid account status.");
            ui.showAdminActions();
            return;
        }

        if (!updatedPin.matches("\\d{5}")) {
            ui.displayError("Invalid pin (must be 5 digits).");
            ui.showAdminActions();
            return;
        }

        if (admin.checkLoginExists(updatedLogin)) {
            ui.displayError("Login already exists!");
            ui.showAdminActions();
            return;
        }

        boolean result = admin.updateExistingAccount(updatedHolder, updatedStatus, updatedLogin, updatedPin, accountNum);
        if (result) {
            ui.displayMessage("Account successfully updated for holder: " + updatedHolder);
        } else {
            ui.displayError("Account update failed.");
        }

        ui.showAdminActions();
    }

    private void searchForAccount() throws SQLException {
        String accountNum = ui.getInput("Enter account number: ");
        if (!accountNum.matches("\\d+")) {
            ui.displayError("Invalid account number (must be numeric).");
            ui.showAdminActions();
            return;
        }

        try (ResultSet accountMatch = admin.checkAccountExists("*", accountNum)) {
            if (accountMatch.next()) {
                ui.displayMessage("\nFound Account with ID: " + accountNum);
                ui.displayMessage("Holder: " + accountMatch.getString("holder"));
                ui.displayMessage("Balance: " + accountMatch.getString("balance"));
                ui.displayMessage("Status: " + accountMatch.getString("status"));
                ui.displayMessage("Login: " + accountMatch.getString("login"));
                ui.displayMessage("PIN: " + accountMatch.getString("pin"));
            } else {
                ui.displayError("Account not found.");
            }
        }

        ui.showAdminActions();
    }
}
