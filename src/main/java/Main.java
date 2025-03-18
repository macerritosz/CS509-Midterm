import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    /*
        Default Admin and Customer Accounts:
        Admin: John123 12345
        Customer: Max123 12345
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ATMModule());
        ATMapp atm = injector.getInstance(ATMapp.class);
        atm.startATM();
    }
}
