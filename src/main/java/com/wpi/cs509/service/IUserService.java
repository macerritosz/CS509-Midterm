package com.wpi.cs509.service;


/**
 * IUserService defines a common interface for user services
 * such as AdministratorService or CustomerService.
 * It provides the ability to present user-specific actions.
 */
public interface IUserService {
    /**
     * @return a string with a hardcoded type
     */
     String getType();

}
