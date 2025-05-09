package com.wpi.cs509.service.AdministratorService;

import com.wpi.cs509.service.UI;

import java.util.Scanner;

public class AdminUI implements UI {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayError(String error) {
        System.out.println("Error:" + error);
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void showAdminActions() {
        System.out.println("Welcome to Administrator Menu");
        System.out.println("1. Create New Account");
        System.out.println("2. Delete Existing Account");
        System.out.println("3. Update Account Information");
        System.out.println("4. Search For Account");
        System.out.println("5. Exit");
    }
}
