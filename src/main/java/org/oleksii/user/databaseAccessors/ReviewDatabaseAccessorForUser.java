package org.oleksii.user.databaseAccessors;

import org.oleksii.reviews.Review;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ReviewDatabaseAccessorForUser extends DatabaseAccessor {
    public static boolean add_review_to_db(String parameterValue1, String parameterValue2, int parameterValue3) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO reviews (rating, description, date, client_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, parameterValue1);
                preparedStatement.setString(2, parameterValue2);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime truncatedDateTime = now.truncatedTo(ChronoUnit.MINUTES);
                java.sql.Timestamp timestampDate = java.sql.Timestamp.valueOf(truncatedDateTime);
                preparedStatement.setTimestamp(3, timestampDate);
                preparedStatement.setInt(4, parameterValue3);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Review> get_reviews_from_db_ordered_by_old_date() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY date DESC ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime());
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    public static ArrayList<Review> get_reviews_from_db_ordered_by_new_date() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY date";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime());
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    public static ArrayList<Review> get_reviews_from_db_ordered_by_worst_rating() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY rating";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime());
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    public static ArrayList<Review> get_reviews_from_db_ordered_by_best_rating() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY rating DESC ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime());
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }
}
