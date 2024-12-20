//import br.edu.ifpb.webFramework.persistence.annotations.Column;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;
import br.edu.ifpb.webFramework.persistence.annotations.Id;
import br.edu.ifpb.webFramework.persistence.annotations.OneToOne;

@Entity(name = "profiles")
public class Profile {
    @Id
    private Long id;

//    @Column(name = "bio", nullable = false)
    private String bio;

//    @Column(name = "username", unique = true, nullable = false)
    private String username;

//    @Column(name = "screenTheme", nullable = false)
    private String screenTheme;

    @OneToOne(mappedBy = "profile")
    private Person person;

    public Profile() {
    }

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

    @Override
    public String toString() {
        return "Profile{" +
                "screenTheme='" + screenTheme + '\'' +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", id=" + id +
                '}';
    }
}
