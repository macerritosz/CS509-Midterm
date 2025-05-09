package com.wpi.cs509;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    /*
        Default Admin and service.CustomerService.Customer Accounts:
        Admin: John123 12345
        service.CustomerService.Customer: Max123 12345
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ATMModule());
        ATMapp atm = injector.getInstance(ATMapp.class);
        atm.startATM();
    }
}
