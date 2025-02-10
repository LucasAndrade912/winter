import br.edu.ifpb.webFramework.Test;
import br.edu.ifpb.webFramework.http.RequestMethod;
import br.edu.ifpb.webFramework.http.Server;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server("localhost", 8888);
        server.start();

        server.addRoute("/test", RequestMethod.GET, (request, response) -> {
            Profile profile = new Profile("Iai meu povo", "fm_luxcax", "dark");
            Phone phone1 = new Phone("Samsung", "Android", 768.0, 100, LocalDate.now());
            Phone phone2 = new Phone("Apple", "iOS", 740.0, 80, LocalDate.now());
            Person person = new Person("Lucas", "lucas@email.com", profile);
//            person.setPhones(List.of(phone1, phone2));
            List<String> a = List.of("Lucas", "Jessye");
            try {
                response.send(200, a);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}