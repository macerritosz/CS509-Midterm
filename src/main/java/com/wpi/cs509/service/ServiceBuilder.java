package com.wpi.cs509.service;

import com.google.inject.Inject;
import com.wpi.cs509.service.AdministratorService.Administrator;
import com.wpi.cs509.service.AdministratorService.AdministratorService;
import com.wpi.cs509.service.CustomerService.Customer;
import com.wpi.cs509.service.CustomerService.CustomerService;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceBuilder implements IUserServiceBuilder {

    /**
     * Default constructor for ServiceBuilder.
     */
    @Inject
    public ServiceBuilder() {}

    /**
     * Creates IUserService object of type Admin or Customer depending on accountType
     *
     * @param conn A connection object with active connection to database
     * @param accountType A string pertaining to account type
     * @param result A result set containing logged-in user's database information
     * @return IUserService that can be AdministratorService or CustomerService
     * @throws SQLException if database error occurs when reading result
     */
    @Override
    public IUserService createUserService(Connection conn, String accountType, ResultSet result) throws SQLException {
        if(accountType.equals("ADMIN")) {
            System.out.println("Admin Login Successful");
            return new AdministratorService(createAdministrator(conn));
        } else {
            System.out.println("Customer Login Successful");
            return new CustomerService(createCustomerFromResultSet(result, conn));
        }
    }

    /**
     * Creates a Customer instance using the provided ResultSet containing the user's data.
     *
     * @param resultSet A result set containing customer database information
     * @param conn A connection object with active connection to database
     * @return A Customer instance
     * @throws SQLException if database error occurs when reading resultSet
     */
    public Customer createCustomerFromResultSet(ResultSet resultSet, Connection conn) throws SQLException {
        int accountID = resultSet.getInt("account_ID");
        int balance = resultSet.getInt("balance");
        return new Customer(accountID, balance, conn);
    }

    /**
     *  Creates an Administrator instance using the provided database connection.
     *
     * @param conn A connection object with active connection to database
     * @return A Administrator Instance
     */
    public Administrator createAdministrator(Connection conn){
        return new Administrator(conn);
    }
}
