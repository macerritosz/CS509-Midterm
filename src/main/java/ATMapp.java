import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ATMapp {
    public static void main(String[] args) {
        ATMapp app = new ATMapp();
        app.startATM();
    }
    /*
        Default Admin and Customer Accounts:
        Admin: John123 12345
        Customer: Max123 12345
     */
    private void startATM() {
        try (Connection conn = MySQLSource.getConnection()){
            DatabaseSchema.createTable();
            IUserType account = promptLogin(conn);
            if(account != null) {
                account.showUserActions();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private IUserType promptLogin(Connection conn) throws SQLException {
        Scanner auth = new Scanner(System.in);

        System.out.println("Please Sign in");
        System.out.print("Enter Login: ");
        String login = auth.nextLine();
        System.out.print("Enter Pin Code: ");
        String pin = auth.nextLine();

        String userQuery = " SELECT * FROM ACCOUNTS WHERE login = ? AND pin = ?";
        try {
            PreparedStatement preparedUserQuery = conn.prepareStatement(userQuery);
            preparedUserQuery.setString(1, login);
            preparedUserQuery.setString(2, pin);
            ResultSet loginResult = preparedUserQuery.executeQuery();

            /* Puts cursor to the first result and checks it, if it doesnt exist, no matched login */
            if(!loginResult.next()) {
                System.out.println("Invalid Login");
            } else {
                String Account_type = loginResult.getString("Account_Type");
                System.out.println("Account Type: " + Account_type);
                if(Account_type.equals("ADMIN")) {
                    System.out.println("Admin Login Successful");
                    return new Administrator(conn);
                } else {
                    System.out.println("Customer Login Successful");
                    return createCustomerFromResultSet(loginResult, conn);
                }
            }
        } catch (SQLException e) {
            System.out.println("Login Query Error: " + e.getMessage());
        }
        return null;
    }

    public Customer createCustomerFromResultSet(ResultSet resultSet, Connection conn) throws SQLException {
        int accountID = resultSet.getInt("account_ID");
        int balance = resultSet.getInt("balance");
        return new Customer(accountID, balance, conn);
    }
}
