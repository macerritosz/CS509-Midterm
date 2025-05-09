package com.wpi.cs509.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The DatabaseConnection interface provides a method to obtain a connection to a database.
 */
public interface DatabaseConnection {
    /**
     * Establishes a connection to the database.
     *
     * @return a Connection object representing the connection to the database
     * @throws SQLException if there is an error while establishing the connection
     */
    Connection getConnection() throws SQLException;
}
