import br.edu.ifpb.webFramework.persistence.Connection;

public class Main {
    public static void main(String[] args) {
        Connection.connect("localhost", 5432, "quentinha", "postgres", "ifpb");
    }
}