import com.wpi.cs509.entity.Administrator;
import com.wpi.cs509.entity.Customer;
import com.wpi.cs509.repository.AdminRepository;
import com.wpi.cs509.repository.CustomerRepository;
import com.wpi.cs509.service.AdministratorService;
import com.wpi.cs509.service.CustomerService;
import com.wpi.cs509.service.IUserService;
import com.wpi.cs509.service.ServiceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServiceBuilderTest {
    private Connection mockConnection;
    private ServiceBuilder serviceBuilder;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private AdminRepository adminRepository;
    private Administrator administrator;
    private CustomerRepository customerRepository;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        adminRepository = mock(AdminRepository.class);
        administrator = new Administrator(adminRepository);
        customerRepository = mock(CustomerRepository.class);
        customer = new Customer(1, 100, customerRepository);
    }

    @Test
    public void testCreateUserService_Admin() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        ServiceBuilder serviceBuilder = new ServiceBuilder(adminRepository, customerRepository);

        IUserService service = serviceBuilder.createUserService("ADMIN", mockResultSet);
        assertInstanceOf(AdministratorService.class, service);
    }

    @Test
    public void testCreateUserService_Customer() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        ServiceBuilder serviceBuilder = new ServiceBuilder(adminRepository, customerRepository);

        IUserService service = serviceBuilder.createUserService("CUSTOMER", mockResultSet);
        assertInstanceOf(CustomerService.class, service);
    }

    @Test
    public void testCreateCustomerFromResultSet_Service() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        ServiceBuilder serviceBuilder = new ServiceBuilder(adminRepository, customerRepository);
        when(mockResultSet.getInt("account_ID")).thenReturn(1);
        when(mockResultSet.getInt("balance")).thenReturn(100);

        Customer service = serviceBuilder.createCustomerFromResultSet( mockResultSet );
        assertInstanceOf(Customer.class, service);
    }

    @Test
    public void testCreateAdministrator() throws SQLException {
        ServiceBuilder serviceBuilder = new ServiceBuilder(adminRepository, customerRepository);
        Administrator service = serviceBuilder.createAdministrator();
        assertInstanceOf(Administrator.class, service);
    }
}
