package org.oleksii.admin.databaseAccessors;

import org.oleksii.reviews.Review;

import java.sql.*;
import java.util.ArrayList;

public class ReviewDatabaseAccessorForAdmin extends DatabaseAccessor {
    public static ArrayList<Review> get_reviews_from_db_ordered_by_old_date_for_admin() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY date DESC ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getInt("client_id"));
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    public static ArrayList<Review> get_reviews_from_db_ordered_by_new_date_for_admin() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY date";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getInt("client_id"));
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    public static ArrayList<Review> get_reviews_from_db_ordered_by_worst_rating_for_admin() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY rating";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getInt("client_id"));
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    public static ArrayList<Review> get_reviews_from_db_ordered_by_best_rating_for_admin() {
        ArrayList<Review> arrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM reviews ORDER BY rating DESC ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Review review = new Review(resultSet.getInt("id"),
                            resultSet.getString("rating"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getInt("client_id"));
                    arrayList.add(review);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }
}
