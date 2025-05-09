package com.wpi.cs509.service.AdministratorService;

public class AdminUtils {

    public boolean checkPIN(String pin) {
        return pin.matches("\\d{5}");
    }
    public boolean checkBalance(String balance){
        try {
            return Double.parseDouble(balance) >= 0;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean checkStatus(String status) {
        return status.equalsIgnoreCase("ACTIVE") || status.equalsIgnoreCase("DISABLED");
    }

    public boolean checkAccNumber (String accountNum) {
        try {
            Integer.parseInt(accountNum);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
