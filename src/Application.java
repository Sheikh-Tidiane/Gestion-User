import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        Role adminRole = new Role(1, "admin");
        Role etudiantRole = new Role(2, "etudiant");
        Role employeRole = new Role(3, "employe");

        addUser(new User(1, "diop42@gmail.com", "password123"), adminRole);
        addUser(new User(2, "sheikh422@gmail.com", "pass456"), etudiantRole);
        addUser(new User(3, "user3@gmail.com", "passer456"), employeRole);
        listUsers();
    }
    private static void addUser(User user, Role role) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String insertUserQuery = "INSERT INTO users (email, passwordHashed, role_id) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getPasswordHashed());
                preparedStatement.setInt(3, role.getId());
                preparedStatement.executeUpdate();
            }
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listUsers() {
        try (Connection connection = DatabaseManager.getConnection()) {
            String selectUsersQuery = "SELECT u.id, u.email, r.name FROM users u JOIN roles r ON u.role_id = r.id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectUsersQuery)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("Liste des utilisateurs :");
                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String userEmail = resultSet.getString("email");
                    String roleName = resultSet.getString("name");
                    System.out.println("ID : " + userId + ", Email : " + userEmail + ", Role : " + roleName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

