
import com.wpi.cs509.service.CustomerService.Customer;
import com.wpi.cs509.service.CustomerService.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

public class CustomerTest {
    private Connection mockConnection;
    private Customer mockCustomer;
    private CustomerService mockCustomerService;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        mockConnection = Mockito.mock(Connection.class);
        mockCustomer = new Customer(1, 100.0,  mockConnection);
    }

    @Test
    public void testUpdateBalance_Success() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        mockCustomer.updateBalance(200.0);

        verify(mockPreparedStatement).setDouble(1, 200);
        verify(mockPreparedStatement).setInt(2,mockCustomer.getAccountID());
        verify(mockPreparedStatement).executeUpdate();

    }
    @Test
    public void testUpdateBalance_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        mockCustomer.updateBalance(200.0);

        verify(mockPreparedStatement).setDouble(1, 200);
        verify(mockPreparedStatement).setInt(2,mockCustomer.getAccountID());
        verify(mockPreparedStatement).executeUpdate();

    }
    @Test
    public void testUpdateBalanceThrowsException() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        mockCustomer.updateBalance(200.0);

    }

    @Test
    public void testGetDatabaseBalance_Success() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("balance")).thenReturn(1000.0);

        mockCustomer.getDataBaseBalance();

        verify(mockPreparedStatement).setInt(1, mockCustomer.getAccountID());
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next();
        verify(mockResultSet).getDouble("balance");

    }


    @Test
    public void testGetDatabaseBalance_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        when(mockResultSet.getDouble("balance")).thenReturn(1000.0);

        mockCustomer.getDataBaseBalance();

        verify(mockPreparedStatement).setInt(1, mockCustomer.getAccountID());
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next();

    }
    @Test
    public void testGetDatabaseBalanceThrowsException() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        mockCustomer.getDataBaseBalance();
    }

}
