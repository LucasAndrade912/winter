import br.edu.ifpb.webFramework.persistence.Connection;
import br.edu.ifpb.webFramework.persistence.EntityManager;
import br.edu.ifpb.webFramework.persistence.QueryBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection.connect("localhost", 5432, "winter", "postgres", "postgres");

        String sql = new QueryBuilder("users")
                .select()
                .join("profiles", "users.profile_id = profiles.id")
                .join("phones", "users.id = phones.user_id")
                .orderBy("users.id")
                .build();

        List<Person> users = EntityManager.executeQuery(sql, Person.class);
        System.out.println(sql);
        for (Person user : users) {
            System.out.println(user);
        }

        Connection.disconnect();
    }
}