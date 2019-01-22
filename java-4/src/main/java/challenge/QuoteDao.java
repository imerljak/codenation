package challenge;

import java.sql.*;
import java.util.Random;

public class QuoteDao {

    public Quote getQuote() throws SQLException {

        final String query = "select actor, detail from scripts limit 1 offset ?";

        try (Connection conn = ConnectionFactory.createConnection()) {

            final int randomIndex = getRandomQuoteIndex(conn);

            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, randomIndex);

                final ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    return new Quote(
                            resultSet.getString("actor"),
                            resultSet.getString("detail")
                    );
                }
            }

        }

        return null;
    }

    public Quote getQuoteByActor(String actor) throws SQLException {
        final String query = "select actor, detail from scripts where actor = ? limit 1 offset ?";

        try (Connection conn = ConnectionFactory.createConnection()) {

            final int quoteIndex = getRandomQuoteIndexByActor(conn, actor);
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, actor);
                stmt.setInt(2, quoteIndex);

                final ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    return new Quote(
                            resultSet.getString("actor"),
                            resultSet.getString("detail")
                    );
                }
            }
        }

        return null;
    }

    private int getRandomQuoteIndexByActor(Connection conn, String actor) throws SQLException {

        final String query = "select count(*) as count from scripts where actor = ?";
        try (final PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, actor);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int quoteCount = resultSet.getInt("count");
                return new Random().nextInt(quoteCount);
            }

            return -1;
        }
    }

    private int getRandomQuoteIndex(Connection connection) throws SQLException {

        final String query = "select count(*) as count from scripts";

        try (Statement stmt = connection.createStatement()) {
            final ResultSet resultSet = stmt.executeQuery(query);

            if (resultSet.next()) {
                int quoteNumber = resultSet.getInt("count");
                return new Random().nextInt(quoteNumber);
            }
        }

        return -1;
    }

}
