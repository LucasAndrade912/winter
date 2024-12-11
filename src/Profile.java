import br.edu.ifpb.webFramework.persistence.annotations.Column;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;

@Entity(name = "profiles")
public class Profile {
    @Column(name = "id", primaryKey = true)
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "screenTheme")
    private String screenTheme;

    @Column(name = "user_id", foreignKey = true, references = "users", referenceId = "id")
    private Person person;

    public Profile(String bio, String username, String screenTheme) {
        this.bio = bio;
        this.username = username;
        this.screenTheme = screenTheme;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScreenTheme() {
        return screenTheme;
    }

    public void setScreenTheme(String screenTheme) {
        this.screenTheme = screenTheme;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
