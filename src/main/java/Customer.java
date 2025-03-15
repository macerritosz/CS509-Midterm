import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer implements IUserType{
    private final ResultSet resultSet;

    private int accountID;
    private int balance;

    public Customer(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    private void setFoundValues(){
        try {
            accountID = resultSet.getInt("account_ID");
            balance = resultSet.getInt("balance");
        } catch (SQLException e) {
            System.out.println("Failed to get Values from Result Set: " + e.getMessage());
        }
    }

    public void showUserActions(){
        System.out.println("Welcome to Customer Menu");
        System.out.println("1. Withdraw Currency");
        System.out.println("2. Deposit Currency");
        System.out.println("3. Display Balance");
        System.out.println("4. Exit");
    }
}
