import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CustomerService implements IUserService {
    private final Customer customer;

    public CustomerService(Customer customer) {
        this.customer = customer;
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
        /*
        Make sure to check what happens if faulty input multiple times
         */
        boolean error = false;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter amount to withdraw: ");
            withdrawAmount = scanner.nextDouble();
            if (withdrawAmount < 0) {
                System.out.println("Invalid Amount. Amount to Withdraw must be greater than 0.");
                error = true;
                continue;
            }
            if (customer.getBalance() < withdrawAmount) {
                System.out.println("Insufficient Balance");
                error = true;
            }
        } while (error);

        double newBalance = customer.getBalance() - withdrawAmount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Withdrawn", withdrawAmount, newBalance);
            showUserActions();
        } else {
            System.out.println("Update Database Balance Failed");
        }
        showUserActions();
    }

    private void deposit() throws SQLException {
        double depositAmount;
        boolean error = false;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter amount to withdraw: ");
            depositAmount = scanner.nextDouble();
            if (depositAmount <= 0) {
                System.out.println("Invalid Amount. Amount to Deposit must be greater than 0.");
                error = true;
            }
        } while (error);

        double newBalance = customer.getBalance() + depositAmount;
        if (customer.updateBalance(newBalance)) {
            printServiceDetails("Deposited", depositAmount, newBalance);
            showUserActions();
        } else {
            System.out.println("Update Database Balance Failed");

        }
        showUserActions();
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
        System.out.println();
    }
}
