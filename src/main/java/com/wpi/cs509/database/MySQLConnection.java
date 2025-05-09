package com.wpi.cs509.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQLConnection class implements the DatabaseConnection interface and provides
 * a method to establish a connection to a MySQL database using JDBC.
 */
public class MySQLConnection implements DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/midterm";
    private static final String USER = "macerr";
    private static final String PASSWORD = "password";

    /**
     * Establishes a connection to the MySQL database.
     *
     * @return a Connection object representing the established connection to the database
     * @throws SQLException if there is an error while establishing the connection
     */
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
