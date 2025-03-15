import javax.sound.midi.SysexMessage;
import java.awt.desktop.SystemEventListener;
import java.sql.*;
import java.util.Scanner;

public class Administrator implements IUserType {
    private final Connection connection;


    public Administrator(Connection connection) {
        this.connection = connection;
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
            case 1: createNewAccount();
            break;
            case 2: deleteExistingAccount();
            break;
            case 3: updateExistingAccount();
            break;
            case 4: searchForAccount();
            break;
            default: System.exit(0);
            break;
        }
    }

    private void createNewAccount() throws SQLException {
        String login;
        String pin;
        String name;
        String balance;
        String status;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Creating new account... ");
        System.out.println("Enter account Login: ");
        login = scanner.nextLine();

        System.out.println("Enter a 5 digit account PIN: ");
        pin = scanner.nextLine();

        System.out.println("Enter account Holder's Name: ");
        name = scanner.nextLine();

        System.out.println("Enter account Starting Balance: ");
        balance = scanner.nextLine();

        System.out.println("Enter account Status: ");
        status = scanner.nextLine();

        /* Validate Data */
        // Login will be validated by insertion failing

        String loginMatchQuery = " SELECT * FROM ACCOUNTS WHERE login = ?";
        PreparedStatement preparedLoginMatch = connection.prepareStatement(loginMatchQuery);
        preparedLoginMatch.setString(1,login);
        ResultSet loginMatch = preparedLoginMatch.executeQuery();
        if(loginMatch.next()) {
            System.out.println("Account Login already exists!");
        }
        //make sure it is 5 digits
        if(!pin.matches("\\d{5}")){
            System.out.println("Invalid pin");
        }

        if(Double.parseDouble(balance) < 0) {
            System.out.println("Initial Balance cannot be negative");
            return;
        }
        if(!status.equalsIgnoreCase("ACTIVE") && !status.equalsIgnoreCase("DISABLED")) {
            System.out.println("Invalid Account Status");
            return;
        } else {
            status = status.toUpperCase();
        }

        String insertStatement = "insert into accounts (holder, balance, login, pin, status) values(?,?,?,?,?)";

        PreparedStatement preparedUserQuery = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        preparedUserQuery.setString(1, name);
        preparedUserQuery.setString(2, balance);
        preparedUserQuery.setString(3, login);
        preparedUserQuery.setString(4, pin);
        preparedUserQuery.setString(5, status);
        int result = preparedUserQuery.executeUpdate();

        if(result == 1) {
            ResultSet rs = preparedUserQuery.getGeneratedKeys();
            if(rs.next()){
                System.out.println("Account Successfully Created -- Account ID: " + rs.getInt(1));
                showUserActions();
            }
        } else {
            System.out.println("Account Creation Failed");
            showUserActions();
        }

    }
    private void deleteExistingAccount() throws SQLException {
        String accountNum;
        String matchNum;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Deleting An account... ");
        System.out.println("Enter account number for account to be deleted: ");
        accountNum = scanner.nextLine();
        String accMatchQuery = " SELECT holder FROM ACCOUNTS WHERE account_id = ?";
        PreparedStatement preparedLoginMatch = connection.prepareStatement(accMatchQuery);
        preparedLoginMatch.setString(1,accountNum);
        ResultSet accountMatch = preparedLoginMatch.executeQuery();
        if(accountMatch.next()) {
            String holder = accountMatch.getString("holder");
            System.out.println("You with to delete the account held by " + holder + " please re-enter the account Number: ");
            matchNum = scanner.nextLine();
            if(accountNum.equals(matchNum)) {
                String deleteStatement = "DELETE FROM accounts WHERE account_id = ?";
                PreparedStatement preparedDeleteStatement = connection.prepareStatement(deleteStatement);
                preparedDeleteStatement.setString(1,accountNum);
                int result = preparedDeleteStatement.executeUpdate();
                if(result == 1) {
                    System.out.println("Account Successfully Deleted -- Account ID: " + accountNum);
                    showUserActions();
                }
            }
        }
        showUserActions();
    }

    private void updateExistingAccount() throws SQLException {
        String accountNum;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Updating An account... ");
        System.out.println("Enter account number for information updating: ");
        accountNum = scanner.nextLine();
        String accMatchQuery = " SELECT holder FROM ACCOUNTS WHERE account_id = ?";
        PreparedStatement preparedLoginMatch = connection.prepareStatement(accMatchQuery);
        preparedLoginMatch.setString(1,accountNum);
        ResultSet accountMatch = preparedLoginMatch.executeQuery();

        if(accountMatch.next()) {
            System.out.println("Found Account with ID: " + accountNum);
            System.out.println("Holder: " + accountMatch.getString("holder"));
            System.out.println("Balance: " + accountMatch.getString("balance"));
            System.out.println("Status: " + accountMatch.getString("status"));
            System.out.println("Login: " + accountMatch.getString("login"));
            System.out.println("PIN: " + accountMatch.getString("pin"));
        }
    }
    private void searchForAccount() {

    }

}
