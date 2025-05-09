import com.wpi.cs509.repository.AdminRepository;
import com.wpi.cs509.entity.Administrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdministratorTest {

    private AdminRepository mockRepo;
    private Administrator administrator;

    @BeforeEach
    void setUp() {
        mockRepo = mock(AdminRepository.class);
        administrator = new Administrator(mockRepo);
    }

    @Test
    void testPushNewAccount_success() throws SQLException {

        String login = "user1";
        String pin = "1234";
        String name = "John Doe";
        String balance = "1000";
        String status = "active";

        when(mockRepo.insertAccount(login, pin, name, balance, status)).thenReturn(1);


        administrator.pushNewAccount(login, pin, name, balance, status);

        verify(mockRepo, times(1)).insertAccount(login, pin, name, balance, status);
    }

    @Test
    void testDeleteExistingAccount_success() throws SQLException {
        String accountId = "1";
        when(mockRepo.deleteAccountById(accountId)).thenReturn(true);

        boolean result = administrator.deleteExistingAccount(accountId);

        assertTrue(result);
        verify(mockRepo, times(1)).deleteAccountById(accountId);
    }

    @Test
    void testUpdateExistingAccount_success() throws SQLException {

        String holder = "John Doe";
        String status = "active";
        String login = "newlogin";
        String pin = "12345";
        String accountId = "1";
        when(mockRepo.updateAccount(holder, status, login, pin, accountId)).thenReturn(true);

        boolean result = administrator.updateExistingAccount(holder, status, login, pin, accountId);

        assertTrue(result);
        verify(mockRepo, times(1)).updateAccount(holder, status, login, pin, accountId);
    }

    @Test
    void testCheckAccountExists_found() throws SQLException {
        String accountId = "1";
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockRepo.findAccount("*", accountId)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        ResultSet resultSet = administrator.checkAccountExists("*", accountId);

        assertNotNull(resultSet);
        verify(mockRepo, times(1)).findAccount("*", accountId);
    }

}
