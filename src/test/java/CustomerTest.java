import com.wpi.cs509.repository.CustomerRepository;
import com.wpi.cs509.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerTest {

    private CustomerRepository mockRepo;
    private Customer customer;

    @BeforeEach
    void setUp() {
        mockRepo = mock(CustomerRepository.class);
        customer = new Customer(1, 1000.0, mockRepo);
    }

    @Test
    void testGetAccountID() {
        assertEquals(1, customer.getAccountID());
    }

    @Test
    void testGetBalance() {
        assertEquals(1000.0, customer.getBalance());
    }

    @Test
    void testSetBalance() {
        customer.setBalance(2000.0);
        assertEquals(2000.0, customer.getBalance());
    }

    @Test
    void testUpdateBalance_success() throws SQLException {
        double newBalance = 1500.0;
        when(mockRepo.updateBalance(1, newBalance)).thenReturn(true);
        boolean result = customer.updateBalance(newBalance);
        assertTrue(result);
        assertEquals(newBalance, customer.getBalance());
        verify(mockRepo, times(1)).updateBalance(1, newBalance);
    }

    @Test
    void testUpdateBalance_failure() throws SQLException {
        double newBalance = 1500.0;
        when(mockRepo.updateBalance(1, newBalance)).thenReturn(false);
        boolean result = customer.updateBalance(newBalance);


        assertFalse(result);
        verify(mockRepo, times(1)).updateBalance(1, newBalance);
    }

    @Test
    void testUpdateBalance_ThrowsError() throws SQLException {
        double newBalance = 1500.0;
        when(mockRepo.updateBalance(1, newBalance)).thenThrow(new SQLException());
        boolean result = customer.updateBalance(newBalance);


        assertFalse(result);
        verify(mockRepo, times(1)).updateBalance(1, newBalance);
    }

    @Test
    void testGetDatabaseBalance_success() throws SQLException {
        double databaseBalance = 1200.0;
        when(mockRepo.getBalance(1)).thenReturn(databaseBalance);
        double result = customer.getDataBaseBalance();
        assertEquals(databaseBalance, result);
        verify(mockRepo, times(1)).getBalance(1);
    }

    @Test
    void testGetDatabaseBalance_failure() throws SQLException {
        when(mockRepo.getBalance(1)).thenThrow(SQLException.class);
        double result = customer.getDataBaseBalance();
        assertEquals(0.0, result);
        verify(mockRepo, times(1)).getBalance(1);
    }
}
