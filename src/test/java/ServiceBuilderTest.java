
import com.wpi.cs509.service.AdministratorService.Administrator;
import com.wpi.cs509.service.AdministratorService.AdministratorService;
import com.wpi.cs509.service.CustomerService.Customer;
import com.wpi.cs509.service.CustomerService.CustomerService;
import com.wpi.cs509.service.IUserService;
import com.wpi.cs509.service.ServiceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServiceBuilderTest {
    private Connection mockConnection;
    private ServiceBuilder serviceBuilder;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testCreateUserService_Admin() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        ServiceBuilder serviceBuilder = new ServiceBuilder();

        IUserService service = serviceBuilder.createUserService(mockConnection, "ADMIN", mockResultSet);
        assertInstanceOf(AdministratorService.class, service);
    }

    @Test
    public void testCreateUserService_Customer() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        ServiceBuilder serviceBuilder = new ServiceBuilder();

        IUserService service = serviceBuilder.createUserService(mockConnection, "CUSTOMER", mockResultSet);
        assertInstanceOf(CustomerService.class, service);
    }

    @Test
    public void testCreateCustomerFromResultSet_Service() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        when(mockResultSet.getInt("account_ID")).thenReturn(1);
        when(mockResultSet.getInt("balance")).thenReturn(100);

        Customer service = serviceBuilder.createCustomerFromResultSet( mockResultSet, mockConnection);
        assertInstanceOf(Customer.class, service);
    }

    @Test
    public void testCreateAdministrator() throws SQLException {
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        Administrator service = serviceBuilder.createAdministrator(mockConnection);
        assertInstanceOf(Administrator.class, service);
    }
}
