package com.wpi.cs509.service;

import com.wpi.cs509.entity.Customer;

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
     * A constructor for a CustomerService using a Customer object to preform service oprations
     *
     * @param customer the Customer instance to preform operations with
     */
    public CustomerService(Customer customer) {
        this.customer = customer;
    }

    /**
     * A function to execute the withdrawal operation
     *
     * @param amount a double to withdraw
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to withdraw must be greater than 0.");
        }
        if (customer.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        double newBalance = customer.getBalance() - amount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Withdrawn", amount, newBalance);
        }
    }

    /**
     * A function to execute the deposit operation
     *
     * @param amount a double to deposit
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to deposit must be greater than 0.");
        }
        double newBalance = customer.getBalance() + amount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Deposited", amount, newBalance);
        }
    }


    /**
     * a function to handle printing the current balance
     *
     * @return a double from the database
     */
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

    /**
     * A function to get the service Type
     *
     * @return a string of the word "customer"
     */
    @Override
    public String getType() {
        return "customer";
    }
}
