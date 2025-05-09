

import com.wpi.cs509.entity.Administrator;
import com.wpi.cs509.service.AdministratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdministratorServiceTest {

    private Administrator mockAdmin;
    private AdministratorService administratorService;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        mockAdmin = mock(Administrator.class);
        administratorService = new AdministratorService(mockAdmin);
    }

    @Test
    void testCreateNewAccount_success() throws SQLException {
        doNothing().when(mockAdmin).pushNewAccount("login", "12345", "test", "1000", "active");

        administratorService.createNewAccount("login", "12345", "test", "1000", "active");

        verify(mockAdmin, times(1))
                .pushNewAccount("login", "12345", "test", "1000", "active");
    }

    @Test
    void testDeleteExistingAccount_ValidAccount() throws SQLException {
        when(mockAdmin.deleteExistingAccount("1")).thenReturn(true);
        boolean result = administratorService.deleteExistingAccount("1");
        assertTrue(result);
        verify(mockAdmin, times(1)).deleteExistingAccount("1");
    }

    @Test
    void testUpdateExistingAccount_ValidAccount() throws SQLException {
        when(mockAdmin.updateExistingAccount("name", "active", "newlogin", "10000", "1")).thenReturn(true);

        Boolean result = administratorService.updateExistingAccount("name", "active", "newlogin", "10000", "1");

        assertTrue(result);
        verify(mockAdmin, times(1))
                .updateExistingAccount("name", "active", "newlogin", "10000", "1");
    }

    @Test
    void testCheckAccountExists_Found() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        when(mockAdmin.checkAccountExists("*", "1")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean res = administratorService.accountExists("1");

        assertTrue(res);
        verify(mockAdmin, times(1)).checkAccountExists("*", "1");

    }

    @Test
    void testCheckAccountDetails_Found() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        when(mockAdmin.checkAccountExists("*", "1")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        ResultSet res = administratorService.getAccountDetails("1");

        assertNotNull(res);
        verify(mockAdmin, times(1)).checkAccountExists("*", "1");

    }
    @Test
    void testCheckGetHolder_Found() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockAdmin.checkAccountExists("holder", "1")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("holder")).thenReturn("John Doe");

        String result = administratorService.getAccountHolder("1");

        assertNotNull(result);
        assertEquals("John Doe", result);
        verify(mockAdmin, times(1)).checkAccountExists("holder", "1");
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getString("holder");
    }

}