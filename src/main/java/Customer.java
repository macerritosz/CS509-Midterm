import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/*
    To do:
    Refactor Customer to use DI, Create CustomerService/Action Class to oversee actual funcitons, and only make customer
    account balance and ID stuff

 */

public class Customer implements IUserType{
    private final Connection connection;
    private int accountID;
    private double balance;

    public Customer(int accountID, double balance, Connection connection) {
        this.accountID = accountID;
        this.balance = balance;
        this.connection = connection;
    }
    public int getAccountID() {
        return accountID;
    }
    public double getBalance() {
        return balance;
    }


    public void showUserActions() throws SQLException {
        System.out.println("Welcome to Customer Menu");
        System.out.println("1. Withdraw Currency");
        System.out.println("2. Deposit Currency");
        System.out.println("3. Display Balance");
        System.out.println("4. Exit");

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
                System.out.println("Exiting...");
                System.exit(0);
                break;
        }
    }

    private void withdraw() throws SQLException {
        /*
        Scanner to get the withdraw number
        Checks: make sure valid integer and that it is above the curtrent balance
        Then modifty the calculatyed valye by updating the value in the DB
         */
        double withdrawAmount;
        double newBalance = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter amount to withdraw: ");
        withdrawAmount = scanner.nextDouble();
        if(getBalance() >= withdrawAmount) {
            newBalance = getBalance() - withdrawAmount;
        } else {
            System.out.println("Insufficient Balance");
        }

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy ");
        LocalDate today = LocalDate.now();
        String formatDate = today.format(myFormatObj);

        String updateBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateBalance);
        preparedStatement.setDouble(1, newBalance);
        preparedStatement.setInt(2, accountID);
        int result = preparedStatement.executeUpdate();

        if(result > 0) {
            System.out.println("Successfully withdrawn");
            System.out.println("Account ID: " + accountID);
            System.out.println("Date: " + formatDate);
            System.out.println("Withdrawn: " + withdrawAmount);
            System.out.println("Balance: " + newBalance);
            showUserActions();
        } else {
            System.out.println("Update Database Balance Failed");

        }
        showUserActions();
    }
    private void deposit() throws SQLException {
        double depositAmount;
        double newBalance = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter amount to deposit: ");
        depositAmount = scanner.nextDouble();
        if(depositAmount >= 0) {
            newBalance = getBalance() + depositAmount;
        } else {
            System.out.println("Invalid Input");
        }

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy ");
        LocalDate today = LocalDate.now();
        String formatDate = today.format(myFormatObj);

        String updateBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateBalance);
        preparedStatement.setDouble(1, newBalance);
        preparedStatement.setInt(2, accountID);
        int result = preparedStatement.executeUpdate();

        if(result > 0) {
            System.out.println("Successfully Deposited");
            System.out.println("Account ID: " + accountID);
            System.out.println("Date: " + formatDate);
            System.out.println("Deposited: " + depositAmount);
            System.out.println("Balance: " + newBalance);
            showUserActions();
        } else {
            System.out.println("Update Database Balance Failed");

        }
        showUserActions();
    }
    private void displayBalance() throws SQLException {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy ");
        LocalDate today = LocalDate.now();
        String formatDate = today.format(myFormatObj);

        String balanceQuery = " SELECT * FROM ACCOUNTS WHERE account_id = ?";
        PreparedStatement preparedLoginMatch = connection.prepareStatement(balanceQuery);
        preparedLoginMatch.setInt(1, accountID);
        ResultSet balanceResult = preparedLoginMatch.executeQuery();
        if(balanceResult.next()) {
            System.out.println("Account ID: " + accountID);
            System.out.println("Date: " + formatDate);
            System.out.println("Balance: " + balanceResult.getDouble("balance"));
        }
        showUserActions();
    }
}
