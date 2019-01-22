package challenge;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection createConnection() throws SQLException {

        try {
            Class.forName("org.sqlite.JDBC");

            final URL resource = ConnectionFactory.class.getClassLoader().getResource("database.sqlite");

            if (resource != null) {
                return DriverManager.getConnection(
                        "jdbc:sqlite:" + resource.getPath());
            } else {
                throw new RuntimeException("Database file not found");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
