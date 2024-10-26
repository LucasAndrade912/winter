import br.edu.ifpb.webFramework.persistence.annotations.Entity;

@Entity
public class User {
    private String name;
    public String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
