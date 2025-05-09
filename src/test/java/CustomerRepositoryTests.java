import com.wpi.cs509.database.DatabaseConnection;
import com.wpi.cs509.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerRepositoryTests {
    private DatabaseConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() throws SQLException {
        mockDbConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDbConnection.getConnection()).thenReturn(mockConnection);

        customerRepository = new CustomerRepository(mockDbConnection);
    }

    @Test
    public void testUpdateBalance_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = customerRepository.updateBalance(1, 1000.0);

        assertTrue(result);
    }
    @Test
    public void testGetBalance_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("balance")).thenReturn(1000.0);

        double result = customerRepository.getBalance(1);

        assertEquals(1000.0, result);
    }

    @Test
    public void testGetBalance_Failure() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        double result = customerRepository.getBalance(1);

        assertEquals(0.0, result);
    }
}
