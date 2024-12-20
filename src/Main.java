import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.DDLHandler;
import br.edu.ifpb.webFramework.persistence.EntityHandler;
//import br.edu.ifpb.webFramework.persistence.EntityManager;
import br.edu.ifpb.webFramework.persistence.QueryBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection.connect("localhost", 5432, "winter", "postgres", "ifpb");

//        DDLHandler.createTable(Profile.class);
//        DDLHandler.createTable(Person.class);
//        DDLHandler.createTable(Phone.class);

//        String sql = new QueryBuilder("users")
//                .select()
//                .join("profiles", "users.profile_id = profiles.id")
//                .orderBy("users.id")
//                .build();
//
//        System.out.println(sql);
//        System.out.println();
//
//        List<Person> users = EntityManager.executeQuery(sql, Person.class);
//
//        users.forEach(user -> {
//            System.out.println("USER: " + user);
//            System.out.println("PROFILE: " + user.getProfile());
//            System.out.println("USER PROFILE: " + user.getProfile().getPerson());
//            System.out.println("-------------");
//        });

        Person johnner = new Person("Johnner", "johnner@gmail.com");
        Person lucas = new Person("Lucas", "johnner@gmail.com");
        Person jessye = new Person("Jessye", "jessye@gmail.com");
        Person julio = new Person("Júlio", "julio@gmail.com");

//        EntityHandler.insert(jessye);
//        EntityHandler.insert(lucas);
//        EntityHandler.insert(johnner);
//        EntityHandler.insert(julio);

        lucas.setEmail("lucas@gmail.com");
        EntityHandler.update(lucas);


        Connection.disconnect();
    }
}