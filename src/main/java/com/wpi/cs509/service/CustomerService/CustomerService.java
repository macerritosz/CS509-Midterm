package com.wpi.cs509.service.CustomerService;

import com.wpi.cs509.service.IUserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * CustomerService provides an interface for customers to manage
 * withdrawals, deposits, and displaying information
 * It implements the IUserService interface.
 */
public class CustomerService implements IUserService {
    private final Customer customer;

    /**
     * A function to allow Customer to withdraw, deposit, or display to account
     *
     * @param customer the Customer instance to preform operations with
     */
    public CustomerService(Customer customer) {
        this.customer = customer;
    }

    /**
     * A function to allow Customer to Withdraw, Deposit, or display balance
     *
     * @throws SQLException if operations encounter a database error
     */
    public void showUserActions() throws SQLException {
        System.out.println("\nWelcome to Customer Menu");
        System.out.println("1. Withdraw Currency");
        System.out.println("2. Deposit Currency");
        System.out.println("3. Display Balance");
        System.out.println("4. Exit\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your choice: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                withdraw();
                break;
            case 2:
                deposit();
                break;
            case 3:
                displayBalance();
                break;
            default:
                System.out.println("Exiting...\n");
                System.exit(0);
                break;
        }
    }

    private void withdraw() throws SQLException {

        String withdrawInput;
        double withdrawAmount = 0;
        boolean error;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter amount to withdraw: ");
            withdrawInput = scanner.nextLine();
            try{
                withdrawAmount = Double.parseDouble(withdrawInput);
                if (withdrawAmount < 0) {
                    System.err.println("Invalid Amount. Amount to Withdraw must be greater than 0.\n");
                    error = true;
                }
                else if (customer.getBalance() < withdrawAmount) {
                    System.err.println("Insufficient Balance\n");
                    error = true;
                } else {
                    error = false;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid Input, Please Retry.\n");
                error = true;
            }
        } while (error);

        double newBalance = customer.getBalance() - withdrawAmount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Withdrawn", withdrawAmount, newBalance);
            showUserActions();
        } else {
            System.err.println("Update Database Balance Failed\n");
            showUserActions();
        }
    }

    private void deposit() throws SQLException {
        String depositInput;
        double depositAmount = 0;
        boolean error;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter amount to Deposit: ");
            depositInput = scanner.nextLine();
            try{
                depositAmount = Double.parseDouble(depositInput);
                if (depositAmount <= 0) {
                    System.out.println("Invalid Amount. Amount to Deposit must be greater than 0.\n");
                    error = true;
                } else {
                    error = false;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid Input, Please Retry.\n");
                error = true;
            }
        } while (error);

        double newBalance = customer.getBalance() + depositAmount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Deposited", depositAmount, newBalance);
            showUserActions();
        } else {
            System.err.println("Update Database Balance Failed\n");
            showUserActions();
        }
    }

    private void displayBalance() throws SQLException {
        printServiceDetails("Display Balance", 0, customer.getDataBaseBalance());
        showUserActions();
    }

    private void printServiceDetails(String serviceType, double serviceAmount, double newBalance) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy ");
        LocalDate today = LocalDate.now();
        String formatDate = today.format(myFormatObj);

        System.out.println("\nSuccessful Transaction:");
        System.out.println("Account ID: " + customer.getAccountID());
        System.out.println("Date: " + formatDate);
        if (!serviceType.equals("Display Balance")) {
            System.out.println(serviceType + ": " + serviceAmount);
        }
        System.out.println("Balance: " + newBalance);
    }
}
