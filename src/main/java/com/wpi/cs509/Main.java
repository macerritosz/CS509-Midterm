package com.wpi.cs509;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main class that serves as the entry point for the ATM application.
 * This class initializes the Guice injector, which is responsible for
 * wiring dependencies and starting the ATM app
 */
public class Main {

    /*
        Default Admin and service.CustomerService.Customer Accounts:
        Admin: John123 12345
        Customer: Max123 12345
     */

    /**
     * Main  method to be executed when application starts, intialized Guice injectors and dependencies and
     * starts the ATM App application
     *
     * @param args cmd line arguments
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ATMModule());
        ATMapp atm = injector.getInstance(ATMapp.class);
        atm.startATM();
    }
}
