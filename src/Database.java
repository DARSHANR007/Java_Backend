


import java.sql.*;

public class Database {



    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    public void connectToDatabase() {

        String updateStatement = "UPDATE students SET name='tom' WHERE student_id=5";


        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college", "root", secrets.passkey);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void displayUsers() {
        String selectStatement = "SELECT * FROM students";

        try {
            resultSet = statement.executeQuery(selectStatement);

            while (resultSet.next()) {
                int id = resultSet.getInt("student_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adduser(String name, String email) throws SQLException {
        String insertStatement = "INSERT INTO students (name, email) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);

        int rowsAffected = preparedStatement.executeUpdate();
        System.out.println("Number of rows affected = " + rowsAffected);
    }

    public void deleteuser(int id) {
        String deleteStatement = "DELETE FROM students WHERE student_id = " + id;

        try {
            int rowsAffected = statement.executeUpdate(deleteStatement);
            System.out.println("Number of rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }








}

