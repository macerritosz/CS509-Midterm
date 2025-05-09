import com.wpi.cs509.database.DatabaseConnection;
import com.wpi.cs509.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminRepositoryTests {

    private DatabaseConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() throws SQLException {
        mockDbConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDbConnection.getConnection()).thenReturn(mockConnection);

        adminRepository = new AdminRepository(mockDbConnection);
    }

    @Test
    void testInsertAccount_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(50);

        int accountId = adminRepository.insertAccount("login", "12345", "name", "1000", "active");

        assertEquals(50, accountId);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testDeleteAccountById_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = adminRepository.deleteAccountById("1");

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateAccount_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = adminRepository.updateAccount("holder", "active", "login", "12345", "1");

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testDoesLoginExist_Found() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean exists = adminRepository.doesLoginExist("login");

        assertTrue(exists);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    void testFindAccount_ReturnsResultSet() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet resultSet = adminRepository.findAccount("*", "1");

        assertNotNull(resultSet);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testInsertAccount_ThrowsException_NoRows() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        SQLException exception = assertThrows(SQLException.class, () -> {
            adminRepository.insertAccount("login", "pin", "name", "1000", "active");
        });

        assertEquals("Account creation failed", exception.getMessage());
    }
    @Test
    public void testFindAccount_TypeHolder() throws SQLException {
        when(mockConnection.prepareStatement(contains("SELECT holder"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet result = adminRepository.findAccount("holder", "1");

        assertNotNull(result);
    }

    @Test
    public void testFindAccount_TypeAll() throws SQLException {
        when(mockConnection.prepareStatement(contains("SELECT *"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet result = adminRepository.findAccount("*", "1");

        assertNotNull(result);
    }
}
