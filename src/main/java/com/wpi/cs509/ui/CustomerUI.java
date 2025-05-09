package com.wpi.cs509.ui;

import com.wpi.cs509.service.CustomerService;

import java.util.Scanner;

public class CustomerUI implements UI {
    private final CustomerService customerService;
    private final Scanner scanner = new Scanner(System.in);

    public CustomerUI( CustomerService customerService) {
        this.customerService = customerService;
    }

    public void showCustomerMenu() {
        boolean exit = false;

        while (!exit) {
            displayMessage("\nWelcome to Customer Menu");
            displayMessage("1. Withdraw Currency");
            displayMessage("2. Deposit Currency");
            displayMessage("3. Display Balance");
            displayMessage("4. Exit\n");

            String choice = getInput("Enter your choice: ");

            switch (choice) {
                case "1" -> handleWithdraw();
                case "2" -> handleDeposit();
                case "3" -> handleDisplayBalance();
                case "4" -> {
                    displayMessage("Exiting...\n");
                    exit = true;
                }
                default -> displayError("Invalid choice. Please try again.");
            }
        }
    }

    private void handleWithdraw() {
        try {
            double amount = getPositiveDoubleInput("Enter amount to withdraw: ");
            customerService.withdraw(amount);
        } catch (IllegalArgumentException e) {
            displayError(e.getMessage());
        } catch (Exception e) {
            displayError("Failed to process withdrawal: " + e.getMessage());
        }
    }

    private void handleDeposit() {
        try {
            double amount = getPositiveDoubleInput("Enter amount to deposit: ");
            customerService.deposit(amount);
        } catch (IllegalArgumentException e) {
            displayError(e.getMessage());
        } catch (Exception e) {
            displayError("Failed to process deposit: " + e.getMessage());
        }
    }

    private void handleDisplayBalance() {
        try {
            double balance = customerService.displayBalance();
            displayMessage("Current Balance: " + balance);
        } catch (Exception e) {
            displayError("Failed to retrieve balance: " + e.getMessage());
        }
    }

    private double getPositiveDoubleInput(String prompt) {
        double amount;
        while (true) {
            try {
                String input = getInput(prompt);
                amount = Double.parseDouble(input);
                if (amount <= 0) {
                    displayError("Amount must be greater than 0.");
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                displayError("Invalid number format. Please try again.");
            }
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayError(String error) {
        System.out.println("Error:" + error);
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
