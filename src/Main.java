import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.EntityManager;
import br.edu.ifpb.webFramework.persistence.QueryBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection.connect("localhost", 5432, "winter", "postgres", "postgres");

//        DDLHandler.createTable(Profile.class);
//        DDLHandler.createTable(Person.class);
//        DDLHandler.createTable(Phone.class);

        String sql = new QueryBuilder("users")
                .select()
                .join("profiles", "users.profile_id = profiles.id")
                .orderBy("users.id")
                .build();

        System.out.println(sql);
        System.out.println();

        List<Person> users = EntityManager.executeQuery(sql, Person.class);

        users.forEach(user -> {
            System.out.println("USER: " + user);
            System.out.println("PROFILE: " + user.getProfile());
            System.out.println("USER PROFILE: " + user.getProfile().getPerson());
            System.out.println("-------------");
        });
//        EntityHandler.insert(lucas);
//        EntityHandler.insert(johnner);
//        EntityHandler.deleteByName(johnner, "Johnner Yelcde");
//        EntityHandler.updateById(johnner, "name", "Yelcde", johnner.getId().toString());

        Connection.disconnect();
    }
}