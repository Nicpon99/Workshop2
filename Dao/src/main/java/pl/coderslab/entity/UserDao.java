package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Arrays;

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

    public User create(User user){
        try (Connection conn = DbUtil.getConnection()){
          PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1, user.getUserName());
          statement.setString(2, user.getEmail());
          statement.setString(3, hashPassword(user.getPassword()));
          statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId){
        try (Connection connection = DbUtil.getConnection()){
            PreparedStatement statement = connection.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword((resultSet.getString("password")));
                return user;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user){
        try (Connection connection = DbUtil.getConnection()){
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1,  user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, this.hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int userId){
        try (Connection connection = DbUtil.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private User[] addToArray(User user, User[] users){
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = user;
        return tmpUsers;
    }

    public User[] findAll(){
        try (Connection connection = DbUtil.getConnection()){
            User[] users = new User[0];
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_USER_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
