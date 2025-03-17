import com.google.inject.AbstractModule;

public class ATMModule extends AbstractModule {
    protected void configure() {
        bind(DatabaseConnection.class).to(MySQLConnection.class);
        bind(IUserServiceBuilder.class).to(ServiceBuilder.class);
    }
}
