package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;

public class UserDao {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE id = ?";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";

    private static final String FIND_ALL_USER_QUERY =
            "SELECT * FROM users";

    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


}
