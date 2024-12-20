import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.DDLHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection.connect("localhost", 5432, "winter", "postgres", "postgres");

        DDLHandler.createTable(Profile.class);
        DDLHandler.createTable(Person.class);
        DDLHandler.createTable(Phone.class);

        Connection.disconnect();
    }
}