import br.edu.ifpb.webFramework.http.RequestMethod;
import br.edu.ifpb.webFramework.http.Server;
import br.edu.ifpb.webFramework.persistence.EntityHandler;
import br.edu.ifpb.webFramework.persistence.QueryHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Class<?>> entities = List.of(Profile.class, Person.class, Phone.class);
        Server server = new Server("localhost", 8889);
        server.start();
        server.initDatabase("localhost", 5432, "winter", "postgres", "postgres", entities);

        server.addRoute("/users/{id}", RequestMethod.GET, (request, response) -> {
            try {
                Map<String, String> parameters = request.getParameters();
                List<Class<Person>> users = null;
                if (!parameters.isEmpty()) {
                     users = QueryHandler.createQuery(Person.class, request.getParameters());
                } else {
                    users = QueryHandler.createQuery(Person.class);
                }

                response.send(200, users);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        server.addRoute("/users", RequestMethod.POST, (request, response) -> {
            try {
                UserDTO requestJson = request.getJson(UserDTO.class);
                Person person = new Person(requestJson.name(), requestJson.email());
                EntityHandler.insert(person);
                response.send(201, person);
            } catch (IOException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        server.addRoute("/test", RequestMethod.PUT, (request, response) -> {
            try {
                response.send(200, "Hello PUT World");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        server.addRoute("/users", RequestMethod.DELETE, (request, response) -> {
            try {
                String id = request.getParameters().get("id");
                List<Class<Person>> list = QueryHandler.createQuery(Person.class, request.getParameters());
                EntityHandler.deleteById(list.get(0), id);
                response.send(200, "Deleted");
            } catch (IOException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}