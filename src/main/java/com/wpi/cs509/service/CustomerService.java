package com.wpi.cs509.service;

import com.wpi.cs509.entity.Customer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    /*Throw the error text up */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to withdraw must be greater than 0.");
        }
        if (customer.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        double newBalance = customer.getBalance() - amount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Withdrawn", amount, newBalance);
            return true;
        }
        return false;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to deposit must be greater than 0.");
        }
        double newBalance = customer.getBalance() + amount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Deposited", amount, newBalance);
            return true;
        }
        return false;
    }


    public double displayBalance(){
        double dbBalance = customer.getDataBaseBalance();
        printServiceDetails("Display Balance", 0, dbBalance);
        return dbBalance;
    }

    private void printServiceDetails(String serviceType, double serviceAmount, double newBalance) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formatDate = LocalDate.now().format(formatter);

        System.out.println("\nSuccessful Transaction:");
        System.out.println("Account ID: " + customer.getAccountID());
        System.out.println("Date: " + formatDate);
        if (!serviceType.equals("Display Balance")) {
            System.out.println(serviceType + ": " + serviceAmount);
        }
        System.out.println("Balance: " + newBalance);
    }

    @Override
    public String getType() {
        return "customer";
    }
}
