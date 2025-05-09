package com.wpi.cs509.ui;

public interface UI {
    void displayMessage(String message);
    void displayError(String error);
    String getInput(String prompt);
}