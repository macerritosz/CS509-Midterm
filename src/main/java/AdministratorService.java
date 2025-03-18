import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AdministratorService implements IUserService {
    private final Administrator admin;

    public AdministratorService(Administrator admin) {
        this.admin = admin;
    }

    public void showUserActions() throws SQLException {
        System.out.println("Welcome to Administrator Menu");
        System.out.println("1. Create New Account");
        System.out.println("2. Delete Existing Account");
        System.out.println("3. Update Account Information");
        System.out.println("4. Search For Account");
        System.out.println("5. Exit");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your choice: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                createNewAccount();
                break;
            case 2:
                deleteExistingAccount();
                break;
            case 3:
                updateExistingAccount();
                break;
            case 4:
                searchForAccount();
                break;
            default:
                System.out.println("Exiting...");
                System.exit(0);
                break;
        }
    }

    private void createNewAccount() throws SQLException {
        String login = "";
        String pin = "";
        String name = "";
        String balance = "";
        String status = "";
        Scanner scanner = new Scanner(System.in);
        boolean error = false;

        System.out.println("Creating new account... ");
        do {
            System.out.println("Enter new account Login: ");
            login = scanner.nextLine();
            if (admin.checkLoginExists(login)) {
                System.err.println("Account already exists! \n");
                error = true;
                continue;
            }

            System.out.println("Enter a 5 digit account PIN: ");
            pin = scanner.nextLine();
            if (!pin.matches("\\d{5}")) {
                System.err.println("Invalid pin \n");
                error = true;
                continue;
            }

            System.out.println("Enter new account Holder's Name: ");
            name = scanner.nextLine();

            System.out.println("Enter new account Starting Balance: ");
            balance = scanner.nextLine();
            if (Double.parseDouble(balance) < 0) {
                System.err.println("Initial Balance cannot be negative \n");
                error = true;
                continue;
            }

            System.out.println("Enter new account Status: ");
            status = scanner.nextLine();
            if (!status.equalsIgnoreCase("ACTIVE") && !status.equalsIgnoreCase("DISABLED")) {
                System.err.println("Invalid Account Status \n");
                error = true;
            } else {
                status = status.toUpperCase();
            }

        } while (error);

        admin.pushNewAccount(login, pin, name, balance, status);
        showUserActions();
    }

    private void deleteExistingAccount() throws SQLException {
        String accountNum;
        String matchNum;
        Scanner scanner = new Scanner(System.in);
        boolean error;
        do {
            System.out.println("Deleting An account... ");
            System.out.println("Enter account number for account to be deleted: ");
            accountNum = scanner.nextLine();
            try {
                Integer.parseInt(accountNum);
                error = false;
            } catch (NumberFormatException e) {
                System.err.println("Invalid account number \n");
                error = true;
            }
        } while (error);
        //check not empty and only numeric
        ResultSet accountMatch = admin.checkAccountExists("holder", accountNum);

        if (accountMatch.next()) {
            String holder = accountMatch.getString("holder");
            System.out.println("You wish to delete the account held by " + holder + ". Please re-enter the account Number: ");
            matchNum = scanner.nextLine();
            try {
                Integer.parseInt(matchNum);
            } catch (NumberFormatException e) {
                System.err.println("Invalid account number \n");
                showUserActions();
            }
            if (accountNum.equals(matchNum)) {
                admin.deleteExistingAccount(matchNum);
                showUserActions();
            } else {
                System.err.println("Numbers Did Not Match, Please pick another action... \n");
                showUserActions();
            }
        } else {
            System.err.println("Account Number is Not Assigned \n");
            showUserActions();
        }
    }

    private void updateExistingAccount() throws SQLException {
        String accountNum;
        String updatedHolder;
        String updatedStatus;
        String updatedLogin;
        String updatedPin;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Updating An account... ");
        while (true) {
            System.out.println("Enter account number for information updating: ");
            accountNum = scanner.nextLine();
            ResultSet accountMatch = admin.checkAccountExists("*", accountNum);

            if (accountMatch.next()) {
                System.out.println("\nAccount Found");
                break;
            } else {
                System.err.println("\nAccount not found");
            }
        }

        while (true) {
            System.out.println("Enter the updated values for the following fields");
            System.out.println("Updated Holder: ");
            updatedHolder = scanner.nextLine();
            System.out.println("Updated Status: ");
            updatedStatus = scanner.nextLine();
            System.out.println("Updated Login: ");
            updatedLogin = scanner.nextLine();
            System.out.println("Updated Pin: ");
            updatedPin = scanner.nextLine();


            if (!updatedStatus.equalsIgnoreCase("ACTIVE") && !updatedStatus.equalsIgnoreCase("DISABLED")) {
                System.err.println("Invalid Account Status\n");
            } else if (!updatedPin.matches("\\d{5}")) {
                System.err.println("Invalid pin\n");
                //prevent the possibility of login being empty
            } else if (admin.checkLoginExists(updatedLogin)){
                System.err.println("Account already exists!\n");
            } else {
                break; //everything works
            }
        }
        /* For simplicity, make sure the input cannot be empty, if so retry */
        admin.updateExistingAccount(updatedHolder, updatedStatus, updatedLogin, updatedPin,accountNum);
        showUserActions();
    }

    private void searchForAccount() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String accountNum;
        System.out.println("Enter account number: ");
        accountNum = scanner.nextLine();
        try {
            Integer.parseInt(accountNum);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Account Number, Please pick another action... \n");
            showUserActions();
        }
        try (ResultSet accountMatch = admin.checkAccountExists("*", accountNum)) {

            if (accountMatch.next()) {
                System.out.println("\nFound Account with ID: " + accountNum);
                System.out.println("Holder: " + accountMatch.getString("HOLDER"));
                System.out.println("Balance: " + accountMatch.getString("balance"));
                System.out.println("Status: " + accountMatch.getString("status"));
                System.out.println("Login: " + accountMatch.getString("login"));
                System.out.println("PIN: " + accountMatch.getString("pin"));
                System.out.println();
                showUserActions();
            } else {
                System.err.println("Account not found\n");
                showUserActions();
            }
        }
    }
}
