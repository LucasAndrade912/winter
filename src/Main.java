import br.edu.ifpb.webFramework.exceptions.ConnectionAlreadyExists;
import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.annotations.Processor;

public class Main {
    public static void main(String[] args) throws ConnectionAlreadyExists, IllegalAccessException {
//        java.sql.Connection manager = Connection.connect("localhost", 5432, "winter", "postgres", "ifpb");
        User user = new User("Lucas", "exemplo@gmail.com");
        Processor.process(user);
//        Connection.disconnect();
    }
}