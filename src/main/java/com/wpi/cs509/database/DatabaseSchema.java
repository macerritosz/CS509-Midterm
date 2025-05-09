package com.wpi.cs509.database;

import java.sql.*;

/**
 * Database Schema holds the code declaration for the accounts table to execute if it does not exist already
 */
public class DatabaseSchema {
    /**
     * Creates the 'accounts' table in the database with the specified schema.
     * If the table already exists, no action is taken.
     *
     * @param dbConnection the connection object access database
     */
    public static void createTable(Connection dbConnection){
        String tableSchema = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                + "holder VARCHAR(50) NOT NULL, "
                + "balance FLOAT NOT NULL, "
                + "login VARCHAR(50) NOT NULL UNIQUE, "
                + "pin CHAR(5) NOT NULL, "
                + "status ENUM('ACTIVE', 'DISABLED') NOT NULL DEFAULT 'ACTIVE',"
                + "account_type ENUM('ADMIN', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER'"
                + " ); ";
        try {
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(tableSchema);
            System.out.println("Accounts Table created");
        }catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
