package com.wpi.cs509.service;

import java.sql.SQLException;

/**
 * IUserService defines a common interface for user services
 * such as AdministratorService or CustomerService.
 * It provides the ability to present user-specific actions.
 */
public interface IUserService {

    /**
     * Displays the available actions or menu options
     * for the specific user type (administrator, customer, etc.).
     *
     * @throws SQLException if a database error occurs during the operation
     */
    void showUserActions() throws SQLException;
}
