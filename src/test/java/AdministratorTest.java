import com.wpi.cs509.service.AdministratorService.Administrator;
import com.wpi.cs509.service.AdministratorService.AdministratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdministratorTest {
    private Connection mockConnection;
    private Administrator mockAdministrator;
    private AdministratorService administratorService;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        mockConnection = Mockito.mock(Connection.class);
        mockAdministrator = new Administrator(mockConnection);
    }
    /* Administrator.java */
    @Test
    public void testPushNewAccount_Success() throws SQLException {
        // Mock for PreparedStatement & ResultSet
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1001);


        mockAdministrator.pushNewAccount("testlogin", "12345", "John Doe", "1000", "ACTIVE");


        verify(mockPreparedStatement).setString(1, "John Doe");
        verify(mockPreparedStatement).setString(2, "1000");
        verify(mockPreparedStatement).setString(3, "testlogin");
        verify(mockPreparedStatement).setString(4, "12345");
        verify(mockPreparedStatement).setString(5, "ACTIVE");
        verify(mockPreparedStatement).executeUpdate();


        verify(mockResultSet).next();
        verify(mockResultSet).getInt(1);
    }

    @Test
    public void testPushNewAccount_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);

        //send statement, and mock update, return 0 for 0 insertions made
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        mockAdministrator.pushNewAccount("testlogin", "12345", "John Doe", "1000", "ACTIVE");

        verify(mockPreparedStatement).executeUpdate();

    }

    /* Delete Account */
    @Test
    public void testDeleteExistingAccount_Success() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        mockAdministrator.deleteExistingAccount("1001");

        verify(mockPreparedStatement).setString(1, "1001");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteExistingAccount_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        mockAdministrator.deleteExistingAccount("1001");

        verify(mockPreparedStatement).executeUpdate();
    }

    /*  updateAccount */
    @Test
    public void testUpdateExistingAccount_Success() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        mockAdministrator.updateExistingAccount(
                "Mark Doe", "Disabled", "newerLogin", "54321", "1001"
        );

        verify(mockPreparedStatement).setString(1, "Mark Doe");
        verify(mockPreparedStatement).setString(2, "Disabled");
        verify(mockPreparedStatement).setString(3, "newerLogin");
        verify(mockPreparedStatement).setString(4, "54321");
        verify(mockPreparedStatement).setString(5, "1001");

        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateExistingAccount_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        mockAdministrator.updateExistingAccount(
                "Mark Doe", "Disabled", "newerLogin", "54321", "1001"
        );

        verify(mockPreparedStatement).executeUpdate();
    }
    /*  Check Exists  */
    @Test
    public void testCheckLoginExists_Success() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean result = mockAdministrator.checkLoginExists("newLogin");

        assertTrue(result);
    }

    @Test
    public void testCheckLoginExists_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean result = mockAdministrator.checkLoginExists("newerLogin");

        assertFalse(result);
    }

    @Test
    public void testCheckLoginExists_throwsException() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

        boolean result = mockAdministrator.checkLoginExists("newLogin");

        assertFalse(result);

    }


    /* checkAccountExists */
    // when type ==
    @Test
    public void testCheckAccountExistsTypeStar_Success() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        //returns RS
        ResultSet set = mockAdministrator.checkAccountExists("*", "1001");
        // all that matters is not null
        assertNotNull(set);
        assertTrue(set.next());
    }

    @Test
    public void testCheckAccountExistsTypeStar_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(false);

        ResultSet set = mockAdministrator.checkAccountExists("*", "1001");
        assertNotNull(set);
        assertFalse(set.next());
    }

    @Test
    public void testCheckAccountExistsTypeHolder_Success() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        //returns RS
        ResultSet set = mockAdministrator.checkAccountExists("holder", "1001");
        // all that matters is not null
        assertNotNull(set);
        assertTrue(set.next());
    }

    @Test
    public void testCheckAccountExistsTypeHolder_Failure() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(false);

        ResultSet set = mockAdministrator.checkAccountExists("holder", "1001");
        assertNotNull(set);
        assertFalse(set.next());
    }

    @Test
    public void testCheckAccountExistsThrowsException() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        //returns RS
        ResultSet set = mockAdministrator.checkAccountExists("*", "1001");
        // all that matters is not null
        assertNull(set);
    }
}
