import com.google.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ATMapp {
    private final DatabaseConnection databaseConnection;
    private final IUserServiceBuilder userServiceBuilder;

    @Inject
    public ATMapp (DatabaseConnection databaseConnection, IUserServiceBuilder userServiceBuilder ) {
        this.databaseConnection = databaseConnection;
        this.userServiceBuilder = userServiceBuilder;
    }

    /*Tightly Coupled
     try (Connection conn = MySQLSource.getConnection()){
            DatabaseSchema.createTable();
            IUserService account = promptLogin(conn);
    */

    public void startATM() {
        try (Connection conn = databaseConnection.getConnection()){
            DatabaseSchema.createTable();
            IUserService account = promptLogin(conn);
            if(account != null) {
                account.showUserActions();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private IUserService promptLogin(Connection conn) throws SQLException {
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

            if(!loginResult.next()) {
                System.out.println("Invalid Login");
                return null;
            } else {
                String account_type = loginResult.getString("Account_Type");
                return userServiceBuilder.createUserService(conn, account_type, loginResult );
            }
        } catch (SQLException e) {
            System.out.println("Login Query Error: " + e.getMessage());
        }
        return null;
    }
}
