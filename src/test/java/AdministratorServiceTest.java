

import com.wpi.cs509.service.AdministratorService.Administrator;
import com.wpi.cs509.service.AdministratorService.AdministratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;


import static org.mockito.Mockito.*;

public class AdministratorServiceTest {

    private Administrator mockAdmin;
    private AdministratorService administratorService;

    @BeforeEach
    void setUp() {
        mockAdmin = mock(Administrator.class);
    }

    @Test
    void testShowUserActions_createNewAccount_success() throws SQLException {
    }

    @Test
    void testDeleteExistingAccount_ValidAccount() throws SQLException {

    }

    @Test
    void testUpdateExistingAccount_ValidAccount() throws SQLException {

    }

    @Test
    void testSearchForAccount_Found() throws SQLException {

    }

    @Test
    void testSearchForAccount_NotFound() throws SQLException {

    }
}