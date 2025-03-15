import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*
        Get the Database Connection here and create the tables if it does not exist
         */
        IUserType account;
        Connection conn = null;
        try {
            conn = MySQLSource.getConnection();
            DatabaseSchema.createTable();

        /*
        Default Admin and Customer Accounts:
        Admin: John123 12345
        Customer: Max123 12345
         */

        System.out.println("Please Sign in");
        Scanner auth = new Scanner(System.in);
        System.out.print("Enter Login: ");
        String login = auth.nextLine();
        System.out.print("Enter Pin Code: ");
        String pin = auth.nextLine();

        String userQuery = " SELECT * FROM ACCOUNTS WHERE login = ? AND pin = ?";
        PreparedStatement preparedUserQuery = conn.prepareStatement(userQuery);
        preparedUserQuery.setString(1, login);
        preparedUserQuery.setString(2, pin);
        ResultSet loginResult = preparedUserQuery.executeQuery();

        if(!loginResult.next()) {
            System.out.println("Invalid Login");
        } else {
            String Account_type = loginResult.getString("Account_Type");
            if(Account_type.equals("Admin")) {
                System.out.println("Admin Login Successful");
                account = new Administrator(loginResult);
            } else {
                System.out.println("Customer Login Successful");
                account = new Customer(loginResult);
            }
        }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void customerMenu(){
        System.out.println("Welcome to Customer Menu");
        System.out.println("1. Withdraw Currency");
        System.out.println("2. Deposit Currency");
        System.out.println("3. Display Balance");
        System.out.println("4. Exit");

    }
    private static void adminMenu(){
        System.out.println("Welcome to Customer Menu");
        System.out.println("1. Create New Account");
        System.out.println("2. Delete Existing Account");
        System.out.println("3. Update Account Information");
        System.out.println("4. Search For Account");
        System.out.println("5. Exit");
    }
}
