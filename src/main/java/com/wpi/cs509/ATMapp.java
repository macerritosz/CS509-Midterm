package com.wpi.cs509;

import com.google.inject.Inject;
import com.wpi.cs509.database.DatabaseConnection;
import com.wpi.cs509.database.DatabaseSchema;
import com.wpi.cs509.service.AdministratorService;
import com.wpi.cs509.service.CustomerService;
import com.wpi.cs509.service.IUserService;
import com.wpi.cs509.service.IUserServiceBuilder;
import com.wpi.cs509.ui.AdminUI;
import com.wpi.cs509.ui.CustomerUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
/**
 * The ATMapp class serves as the main entry point for interacting with the ATM system.
 * It manages the user login process and serves as a controller to initiate the correct
 * user services based on the account type.
 */
public class ATMapp {
    private final DatabaseConnection databaseConnection;
    private final IUserServiceBuilder userServiceBuilder;

    /**
     * Constructor to initialize ATMapp with the provided database connection
     * and user service builder.
     *
     * @param databaseConnection A DatabaseConnection instance used to connect to the database
     * @param userServiceBuilder A IUserServiceBuilder instance used to create user service objects
     */
    @Inject
    public ATMapp (DatabaseConnection databaseConnection, IUserServiceBuilder userServiceBuilder ) {
        this.databaseConnection = databaseConnection;
        this.userServiceBuilder = userServiceBuilder;
    }

    /*Tightly Coupled
     try (Connection conn = MySQLSource.getConnection()){
            database.DatabaseSchema.createTable();
            service.IUserService account = promptLogin(conn);
    */

    /**
     * Starts the ATM application by creating a connection to the database,
     * creating necessary tables, and prompting the user for login credentials.
     * If login is successful, the appropriate user actions are displayed.
     */
    public void startATM() {
        try (Connection conn = databaseConnection.getConnection()){
            DatabaseSchema.createTable(conn);
            IUserService account = promptLogin(conn);
            if(account != null) {
                String accountType = account.getType();
                if(accountType.equalsIgnoreCase("admin")) {
                    AdminUI adminUI = new AdminUI((AdministratorService) account);
                    adminUI.showAdminMenu();
                } else {
                    CustomerUI customerUI = new CustomerUI((CustomerService) account);
                    customerUI.showCustomerMenu();
                }
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
                return userServiceBuilder.createUserService(account_type, loginResult );
            }
        } catch (SQLException e) {
            System.out.println("Login Query Error: " + e.getMessage());
        }
        return null;
    }
}
