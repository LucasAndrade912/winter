import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.DDLHandler;
import br.edu.ifpb.webFramework.persistence.EntityHandler;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection.connect("localhost", 5432, "winter", "postgres", "postgres");

        DDLHandler.createTable(Profile.class);
        DDLHandler.createTable(Person.class);
        DDLHandler.createTable(Phone.class);

        Phone phone1 = new Phone("BrandA", "OS1", 6.1, 3000, LocalDate.of(2022, 1, 1));
        Phone phone2 = new Phone("BrandB", "OS2", 5.8, 3500, LocalDate.of(2021, 5, 15));
        Phone phone3 = new Phone("BrandC", "OS3", 6.5, 4000, LocalDate.of(2020, 8, 20));
        Phone phone4 = new Phone("BrandD", "OS4", 6.0, 3200, LocalDate.of(2019, 12, 10));

        Profile profile1 = new Profile("Hello", "pr1", "dark");
        Profile profile2 = new Profile("Opa", "pr2", "dark");
        Profile profile3 = new Profile("Iai", "pr3", "light");

        Person person1 = new Person("João", "joao@email.com", profile1);
        person1.setPhones(List.of(phone1, phone2));

        Person person2 = new Person("Maria", "maria@email.com", profile2);

        Person person3 = new Person("José", "jose@email.com", profile3);
        person3.setPhones(List.of(phone3, phone4));

        EntityHandler.insert(person1);
        EntityHandler.insert(person2);
        EntityHandler.insert(person3);

        Connection.disconnect();
    }
}