import java.sql.*;
import redis.clients.jedis.Jedis;

public class StudentService {
    Connection connection;
    Jedis redisClient;

    public StudentService() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "password");
            redisClient = new Jedis("localhost");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStudent(int id, String name, String email) throws SQLException {
        String insertStatement = "INSERT INTO students (id, name, email) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, email);
        preparedStatement.executeUpdate();

        redisClient.hset("student:" + id, "name", name);
        redisClient.hset("student:" + id, "email", email);
    }

    public void getStudent(int id) throws SQLException {
        if (redisClient.exists("student:" + id)) {
            System.out.println("Name: " + redisClient.hget("student:" + id, "name"));
            System.out.println("Email: " + redisClient.hget("student:" + id, "email"));
        } else {
            String query = "SELECT * FROM students WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                redisClient.hset("student:" + id, "name", name);
                redisClient.hset("student:" + id, "email", email);

                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
            }
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String deleteStatement = "DELETE FROM students WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

        redisClient.del("student:" + id);
    }
}
