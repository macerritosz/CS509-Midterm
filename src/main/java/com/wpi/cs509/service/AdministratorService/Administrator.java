package com.wpi.cs509.service.AdministratorService;

import com.wpi.cs509.service.repository.AdminRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Administrator {
    private final AdminRepository adminRepository;

    public Administrator(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public int pushNewAccount(String login, String pin, String name, String balance, String status) throws SQLException {
        return adminRepository.insertAccount(login, pin, name, balance, status);
    }

    public boolean deleteExistingAccount(String accountId) throws SQLException {
        return adminRepository.deleteAccountById(accountId);
    }

    public boolean updateExistingAccount(String holder, String status, String login, String pin, String accountId) throws SQLException {
        return adminRepository.updateAccount(holder, status, login, pin, accountId);
    }

    public boolean checkLoginExists(String login) throws SQLException {
        return adminRepository.doesLoginExist(login);
    }

    public ResultSet checkAccountExists(String queryType, String accountId) throws SQLException {
        return adminRepository.findAccount(queryType, accountId);
    }
}
