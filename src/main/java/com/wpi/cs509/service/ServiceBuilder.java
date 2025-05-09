package com.wpi.cs509.service;

import com.google.inject.Inject;
import com.wpi.cs509.entity.Administrator;
import com.wpi.cs509.entity.Customer;
import com.wpi.cs509.repository.CustomerRepository;
import com.wpi.cs509.repository.AdminRepository;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ServiceBuilder is responsible for creating IUserService objects for either an Administrator or Customer
 * data from the database to determine the appropriate service to be returned.
 */
public class ServiceBuilder implements IUserServiceBuilder {
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    /**
     * Constructor for ServiceBuilder injected with Admin and Customer Repositories
     */
    @Inject
    public ServiceBuilder(AdminRepository adminRepository, CustomerRepository customerRepository) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
    }
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
            return new AdministratorService(createAdministrator());
        } else {
            System.out.println("Customer Login Successful");
            return new CustomerService(createCustomerFromResultSet(result));
        }
    }

    /**
     * Creates a Customer instance using the provided ResultSet containing the user's data.
     *
     * @param resultSet A result set containing customer database information
     * @return A Customer instance
     * @throws SQLException if database error occurs when reading resultSet
     */
    public Customer createCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        int accountID = resultSet.getInt("account_ID");
        int balance = resultSet.getInt("balance");
        return new Customer(accountID, balance, customerRepository);
    }

    /**
     *  Creates an Administrator instance using the provided database connection.
     *
     * @return A Administrator Instance
     */
    public Administrator createAdministrator(){
        return new Administrator(adminRepository);
    }
}
