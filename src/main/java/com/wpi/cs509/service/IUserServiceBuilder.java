package com.wpi.cs509.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * IUserServiceBuilder is responsible for creating instances of
 * IUserService based on the provided account type and user data.
 */
public interface IUserServiceBuilder {

    /**
     * Creates IUserService object of type Admin or Customer to preform operations with
     *
     * @param conn A connection object with active connection to database
     * @param accountType A string pertaining to account type
     * @param result A result set containing logged-in user's database information
     * @return IUserService that can be AdministratorService or CustomerService
     * @throws SQLException if database error occurs when reading result
     */
     IUserService createUserService(Connection conn, String accountType, ResultSet result) throws SQLException;
}
