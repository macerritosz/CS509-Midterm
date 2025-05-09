package com.wpi.cs509;

import com.google.inject.AbstractModule;
import com.wpi.cs509.database.DatabaseConnection;
import com.wpi.cs509.database.MySQLConnection;
import com.wpi.cs509.service.IUserServiceBuilder;
import com.wpi.cs509.service.ServiceBuilder;

/**
 * Guice dependency Injection module to bind interfaces to concrete implementations
 */
public class ATMModule extends AbstractModule {
    protected void configure() {
        bind(DatabaseConnection.class).to(MySQLConnection.class);
        bind(IUserServiceBuilder.class).to(ServiceBuilder.class);
    }
}
