import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        AuthService authService = new AuthService();
        StudentService studentService = new StudentService();

        authService.register("admin", "admin123", "admin");

        String token = String.valueOf(authService.login("admin", "admin123"));
        System.out.println("Generated JWT: " + token);

        studentService.addStudent(1, "Alice", "alice@example.com", token, authService);
        studentService.getStudent(1, token, authService);
        studentService.deleteStudent(1, token, authService);
    }
}
