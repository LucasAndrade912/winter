import br.edu.ifpb.webFramework.persistence.annotations.Column;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
public class Person {
    @Column(name = "id", primaryKey = true)
    private Long id;

    @Column(name = "name", notNull = true)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    private Profile profile;

    private List<Phone> phones = new ArrayList<>();

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Person(String name, String email, Profile profile) {
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
