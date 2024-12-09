import br.edu.ifpb.webFramework.exceptions.ConnectionAlreadyExists;
import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.annotations.Processor;

public class Main {
    public static void main(String[] args) throws ConnectionAlreadyExists, IllegalAccessException {
        Connection.connect("localhost", 5432, "winter", "postgres", "ifpb");
//        Processor.process(User.class);
//        Processor.process(Phone.class);

        // Criando Usuário
        User newUsuario = new User("Johnner Yelcde", "johnner@gmail.com");
        Processor.processInsert(newUsuario);
        Connection.disconnect();
    }
}