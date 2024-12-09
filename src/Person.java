import br.edu.ifpb.webFramework.persistence.annotations.Entity;
import br.edu.ifpb.webFramework.persistence.annotations.OneToOne;

@Entity
public class Person {
    private String name;
    private String email;

    @OneToOne(mappedBy = "person")
    private Phone phone;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
