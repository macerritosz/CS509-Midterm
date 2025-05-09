package com.wpi.cs509.ui;

import com.wpi.cs509.service.AdministratorService;

import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.SQLException;

public class AdminUI implements UI {
    private final AdministratorService adminService;
    private final Scanner scanner = new Scanner(System.in);

    public AdminUI(AdministratorService adminService) {
        this.adminService = adminService;
    }

    public void showAdminMenu() {
        boolean exit = false;

        while (!exit) {
            displayMessage("\nWelcome to Administrator Menu");
            displayMessage("1. Create New Account");
            displayMessage("2. Delete Existing Account");
            displayMessage("3. Update Account Information");
            displayMessage("4. Search For Account");
            displayMessage("5. Exit\n");

            String choice = getInput("Enter your choice: ");

            switch (choice) {
                case "1" -> handleCreateAccount();
                case "2" -> handleDeleteAccount();
                case "3" -> handleUpdateAccount();
                case "4" -> handleSearchAccount();
                case "5" -> {
                    displayMessage("Exiting...\n");
                    exit = true;
                }
                default -> displayError("Invalid choice. Please try again.");
            }
        }
    }

    private void handleCreateAccount() {

        displayMessage("Creating new account... ");
        //compress do -> while into func that takes func as parameter
        String login = loopUntilValid("Enter new account Login: ", value -> !value.isEmpty() );
        String pin = loopUntilValid("Enter new account Pin: ", value -> value.matches("\\d{5}") );
        String name = loopUntilValid("Enter new account Name: ", value -> !value.isEmpty() );
        String balance = loopUntilValid("Enter new account Balance: ", value -> !value.isEmpty() );
        String status = loopUntilValid("Enter new account Status: ", value -> value.equalsIgnoreCase("ACTIVE") || value.equalsIgnoreCase("DISABLED") );


        try {
            adminService.createNewAccount(login, pin, name, balance, status);
        } catch (SQLException e) {
            displayError("Failed to create account: " + e.getMessage());
        } catch (Exception e) {
            displayError( e.getMessage());
        }
    }

    private void handleDeleteAccount() {
        displayMessage("Deleting an account...");

        String accountNum = loopUntilValid(
                "Enter account number for account to be deleted: ",
                value -> !value.isEmpty() && value.matches("\\d+"));

        try {
            String holder = adminService.getAccountHolder(accountNum);
            if (holder == null) {
                displayError("Account number is not assigned.");
                return;
            }

            String matchNum = loopUntilValid(
                    "You wish to delete the account held by " + holder + ". Please re-enter the account number: ",
                    value -> value.equals(accountNum));

            boolean result = adminService.deleteExistingAccount(accountNum);
            if (result) {
                displayMessage("Account successfully deleted -- Account ID: " + accountNum);
            } else {
                displayError("Account deletion failed.");
            }

        } catch (SQLException e) {
            displayError("Failed to delete account: " + e.getMessage());
        }
    }

    private void handleUpdateAccount() {
        String accountNum = loopUntilValid("Enter account number to update: ", value -> value.matches("\\d+"));

        try {
            boolean exists = adminService.accountExists(accountNum);
            if (!exists) {
                displayError("Account not found.");
                return;
            }

            String updatedHolder = getInput("Updated Holder: ");
            String updatedStatus = loopUntilValid("Updated Status (ACTIVE/DISABLED): ",
                    value -> value.equalsIgnoreCase("ACTIVE") || value.equalsIgnoreCase("DISABLED"));
            String updatedLogin = getInput("Updated Login: ");
            String updatedPin = loopUntilValid("Updated 5-digit Pin: ", value -> value.matches("\\d{5}"));

            boolean result = adminService.updateExistingAccount(updatedHolder, updatedStatus, updatedLogin, updatedPin, accountNum);
            if (result) {
                displayMessage("Account successfully updated for holder: " + updatedHolder);
            } else {
                displayError("Account update failed.");
            }

        } catch (SQLException e) {
            displayError("Failed to update account: " + e.getMessage());
        }
    }

    private void handleSearchAccount() {
        String accountNum = loopUntilValid("Enter account number: ", value -> value.matches("\\d+"));

        try {
            ResultSet details = adminService.getAccountDetails(accountNum);
            if (details == null) {
                displayError("Account not found.");
            } else {
                details.next();
                displayMessage("\nFound Account with ID: " + accountNum);
                displayMessage("Holder: " + details.getString("holder"));
                displayMessage("Balance: " + details.getString("balance"));
                displayMessage("Status: " + details.getString("status"));
                displayMessage("Login: " + details.getString("login"));
                displayMessage("PIN: " + details.getString("pin"));
            }
        } catch (SQLException e) {
            displayError("Failed to search for account: " + e.getMessage());
        }
    }
    // create a function that checks based on another function
    private String loopUntilValid(String input, Validator validator ){
        String currLoopVal;
        boolean isValid;
        do {
            currLoopVal =  getInput(input);
            isValid = validator.validate(currLoopVal);
            if(!isValid) displayError("Invalid input. Please try again.");
        } while (!isValid);
        return currLoopVal;
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayError(String error) {
        System.out.println("Error: " + error);
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    interface Validator {
        boolean validate(String input);
    }
}
