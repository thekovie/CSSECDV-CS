package Model;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private int id;
    private String username;
    private final String hashedPassword;
    private int role = 2;
    private int locked = 0;

    public User(String username, String password){
        this.username = username;
        this.hashedPassword = hashPassword(password);
    }
    
    public User(int id, String username, String password, int role, int locked){
        this.id = id;
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.role = role;
        this.locked = locked;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        System.out.println("Password: " + hashedPassword);
        return hashedPassword;
    }
    
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(8));
    }
    
    public boolean checkPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.hashedPassword);
    }
    
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }
}
