package diploma.elders.up.dao.documents;

import diploma.elders.up.dao.UserRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Simonas on 5/10/2016.
 */
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private UserRole role;

    public User(){}

    public User(String username, String password, UserRole role){

        this.setUsername(username);
        this.setPassword(password);
        this.setRole(role);

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + "'" +
                ", username='" + username + "'" +
                ", password='" + password + "'" +
                ", role='" + role + "'" +
                '}';
    }

}
