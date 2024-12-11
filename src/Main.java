import br.edu.ifpb.webFramework.exceptions.ConnectionAlreadyExists;
import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.annotations.Processor;

public class Main {
    public static void main(String[] args) throws ConnectionAlreadyExists, IllegalAccessException {
        Connection.connect("localhost", 5432, "winter", "postgres", "postgres");
        Processor.process(Person.class);
        Processor.process(Phone.class);
        Processor.process(Profile.class);

        // Criando Usuário
//        Person person1 = new Person("Lucas", "lucas@email.com");
//
//        Profile profile1 = new Profile("Olá galera", "yelcde", "dark");
//        Person person2 = new Person("Johnner", null, profile1);
//
//
//        Processor.processInsert(person1);
//        Processor.processInsert(profile1);
//        Processor.processInsert(person2);
        Connection.disconnect();
    }
}