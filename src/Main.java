import br.edu.ifpb.webFramework.http.RequestMethod;
import br.edu.ifpb.webFramework.http.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server("localhost", 8889);
        server.start();

        server.addRoute("/test", RequestMethod.GET, (request, response) -> {
            try {
                response.send(200, "Hello GET World");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        server.addRoute("/test", RequestMethod.POST, (request, response) -> {
            try {
                response.send(200, "Hello POST World");
            } catch (IOException e) {
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

        server.addRoute("/test", RequestMethod.DELETE, (request, response) -> {
            try {
                response.send(200, "Hello DELETE World");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}