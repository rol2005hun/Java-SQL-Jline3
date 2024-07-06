import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class Database {
    private static Dotenv dotenv;
    private static final String url;
    private static final String username;
    private static final String password;

    static {
        try {
            boolean isProduction = false;
            if (isProduction) {
                url = System.getenv("DB_URL");
                username = System.getenv("DB_USERNAME");
                password = System.getenv("DB_PASSWORD");
            } else {
                dotenv = Dotenv.configure().load();
                url = dotenv.get("DB_URL");
                username = dotenv.get("DB_USERNAME");
                password = dotenv.get("DB_PASSWORD");
            }
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Sikertelen adatbázis csatlakozás: ", e);
        } catch (DotenvException e) {
            throw new RuntimeException("Hiba az env fájl betöltése közben: ", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static List<String[]> executeQuery(String query) {
        List<String[]> results = new ArrayList<>();
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getString(i);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void executeQueryWithPrint(String query) {
        List<String[]> results = executeQuery(query);

        for (String[] row : results) {
            for (String column : row) {
                System.out.print(column + ";;;");
            }
            System.out.println();
        }
    }

    public static void executeUpdate(String query) {
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Tesztelés
        List<String[]> results = executeQuery("select * from users");

        for (String[] row : results) {
            for (String column : row) {
                System.out.print(column + ";;;");
            }
            System.out.println();
        }
    }
}