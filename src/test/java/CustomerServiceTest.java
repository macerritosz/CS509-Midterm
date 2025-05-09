
import com.wpi.cs509.entity.Customer;
import com.wpi.cs509.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {
    private Customer mockCustomer;
    private CustomerService customerService;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        mockCustomer = mock(Customer.class);
        customerService = new CustomerService(mockCustomer);
    }

    @Test
    public void withdrawAmountLessThanOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> customerService.withdraw(-1));
    }

    @Test
    public void withdrawAmountGreaterThanBalance() {
        when(mockCustomer.getBalance()).thenReturn(10.0);
        assertThrows(IllegalArgumentException.class, () -> customerService.withdraw(20));
    }

    @Test
    public void withdrawValidAmount() {
        when(mockCustomer.getBalance()).thenReturn(200.0);
        when(mockCustomer.updateBalance(anyDouble())).thenReturn(true);

        customerService.withdraw(100.0);

        verify(mockCustomer, times(1)).updateBalance(anyDouble());
    }

    @Test
    public void depositAmountLessThanOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> customerService.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> customerService.deposit(-10));
    }
    @Test
    public void depositValidAmount() {
        when(mockCustomer.getBalance()).thenReturn(200.0);
        when(mockCustomer.updateBalance(anyDouble())).thenReturn(true);

        customerService.deposit(100.0);

        verify(mockCustomer, times(1)).updateBalance(anyDouble());
    }
    @Test
    public void displayBalanceReturnsDatabaseBalance() {
        when(mockCustomer.getDataBaseBalance()).thenReturn(200.0);

        double balance = customerService.displayBalance();

        assertEquals(200.0, balance);
        verify(mockCustomer, times(1)).getDataBaseBalance();
    }

}
