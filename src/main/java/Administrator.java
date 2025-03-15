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
            if (checkLoginExists(login)) {
                System.out.println("Account already exists! \n");
                error = true;
                continue;
            }

            System.out.println("Enter a 5 digit account PIN: ");
            pin = scanner.nextLine();
            if (!pin.matches("\\d{5}")) {
                System.out.println("Invalid pin \n");
                error = true;
                continue;
            }

            System.out.println("Enter new account Holder's Name: ");
            name = scanner.nextLine();

            System.out.println("Enter new account Starting Balance: ");
            balance = scanner.nextLine();
            if (Double.parseDouble(balance) < 0) {
                System.out.println("Initial Balance cannot be negative \n");
                error = true;
                continue;
            }

            System.out.println("Enter new account Status: ");
            status = scanner.nextLine();
            if (!status.equalsIgnoreCase("ACTIVE") && !status.equalsIgnoreCase("DISABLED")) {
                System.out.println("Invalid Account Status \n");
                error = true;
            } else {
                status = status.toUpperCase();
            }

        } while (error);

        String insertStatement = "insert into accounts (holder, balance, login, pin, status) values(?,?,?,?,?)";

        PreparedStatement preparedUserQuery = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        preparedUserQuery.setString(1, name);
        preparedUserQuery.setString(2, balance);
        preparedUserQuery.setString(3, login);
        preparedUserQuery.setString(4, pin);
        preparedUserQuery.setString(5, status);
        int result = preparedUserQuery.executeUpdate();

        if (result == 1) {
            ResultSet rs = preparedUserQuery.getGeneratedKeys();
            if (rs.next()) {
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
        //check not empty and only numeric

        String accMatchQuery = " SELECT holder FROM ACCOUNTS WHERE account_id = ?";
        PreparedStatement preparedLoginMatch = connection.prepareStatement(accMatchQuery);
        preparedLoginMatch.setString(1, accountNum);
        ResultSet accountMatch = preparedLoginMatch.executeQuery();

        if (accountMatch.next()) {
            String holder = accountMatch.getString("holder");
            System.out.println("You with to delete the account held by " + holder + " please re-enter the account Number: ");
            matchNum = scanner.nextLine();
            if (accountNum.equals(matchNum)) {
                String deleteStatement = "DELETE FROM accounts WHERE account_id = ?";
                PreparedStatement preparedDeleteStatement = connection.prepareStatement(deleteStatement);
                preparedDeleteStatement.setString(1, accountNum);
                int result = preparedDeleteStatement.executeUpdate();
                if (result == 1) {
                    System.out.println("Account Successfully Deleted -- Account ID: " + accountNum);
                    showUserActions();
                }
            }
        }
        showUserActions();
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

            String accMatchQuery = " SELECT * FROM ACCOUNTS WHERE account_id = ?";
            PreparedStatement preparedLoginMatch = connection.prepareStatement(accMatchQuery);
            preparedLoginMatch.setString(1, accountNum);
            ResultSet accountMatch = preparedLoginMatch.executeQuery();

            if (accountMatch.next()) {
                System.out.println("Account Found");
                break;
            } else {
                System.out.println("Account not found");
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
                System.out.println("Invalid Account Status");
            } else if (!updatedPin.matches("\\d{5}")) {
                System.out.println("Invalid pin");
                //prevent the possibility of login being empty
            } else if (checkLoginExists(updatedLogin)){
                System.out.println("Account already exists!");
            } else {
                break;
            }
        }
        /* For simplicity, make sure the input cannot be empty, if so retry */
        String updateAccountInfo = "UPDATE accounts SET holder = ?, status = ?, login = ?, pin = ? WHERE account_id = ?";
        PreparedStatement preparedUpdateStatement = connection.prepareStatement(updateAccountInfo);
        preparedUpdateStatement.setString(1, updatedHolder);
        preparedUpdateStatement.setString(2, updatedStatus);
        preparedUpdateStatement.setString(3, updatedLogin);
        preparedUpdateStatement.setString(4, updatedPin);
        preparedUpdateStatement.setString(5, accountNum);
        int result = preparedUpdateStatement.executeUpdate();
        if (result == 1) {
            System.out.println("Account Successfully Updated -- Account ID: " + updatedHolder);
            showUserActions();
        }

    }

    private void searchForAccount() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String accountNum;
        System.out.println("Enter account number: ");
        accountNum = scanner.nextLine();

        String accMatchQuery = " SELECT * FROM ACCOUNTS WHERE account_id = ?";
        PreparedStatement preparedLoginMatch = connection.prepareStatement(accMatchQuery);
        preparedLoginMatch.setString(1, accountNum);
        ResultSet accountMatch = preparedLoginMatch.executeQuery();

        if (accountMatch.next()) {
            System.out.println("Found Account with ID: " + accountNum);
            System.out.println("Holder: " + accountMatch.getString("holder"));
            System.out.println("Balance: " + accountMatch.getString("balance"));
            System.out.println("Status: " + accountMatch.getString("status"));
            System.out.println("Login: " + accountMatch.getString("login"));
            System.out.println("PIN: " + accountMatch.getString("pin"));
            showUserActions();
        } else {
            System.out.println("Account not found");
            showUserActions();
        }
    }

    private boolean checkLoginExists(String login) {
        try {
            String loginMatchQuery = " SELECT * FROM ACCOUNTS WHERE login = ?";
            PreparedStatement preparedLoginMatch = connection.prepareStatement(loginMatchQuery);
            preparedLoginMatch.setString(1, login);
            ResultSet loginMatch = preparedLoginMatch.executeQuery();
            return loginMatch.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
