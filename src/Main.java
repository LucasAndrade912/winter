import br.edu.ifpb.webFramework.exceptions.ConnectionAlreadyExists;
import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.annotations.Processor;

public class Main {
    public static void main(String[] args) throws ConnectionAlreadyExists, IllegalAccessException {
        Connection.connect("localhost", 5432, "winter", "postgres", "postgres");
        Processor.process(Person.class);
        Processor.process(Phone.class);
        Connection.disconnect();
    }
}