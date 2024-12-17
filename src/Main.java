import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.DDLHandler;
import br.edu.ifpb.webFramework.persistence.EntityHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection.connect("localhost", 5432, "winter", "postgres", "ifpb");

//        DDLHandler.createTable(Profile.class);
//        DDLHandler.createTable(Person.class);
//        DDLHandler.createTable(Phone.class);

        EntityHandler.insert(new Person("Johnner Yelcde", "johnner@gmail.com"));
        Connection.disconnect();
    }
}