package org.oleksii.user.databaseAccessors;

import org.oleksii.admin.promotional_code_for_admin.Promo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PromoDatabaseAccessorForUser extends DatabaseAccessor {

    public static ArrayList<Promo> get_promos_from_db() {
        ArrayList<Promo> promoArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM promos ORDER BY id ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Promo promo = new Promo(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("discount"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("last_modified_date").toLocalDateTime(),
                            resultSet.getTimestamp("end_date").toLocalDateTime(),
                            resultSet.getBoolean("is_active"));
                    promoArrayList.add(promo);
                }
            }
        } catch (SQLException ignored) {
        }
        return promoArrayList;
    }

    public static void delete_promo_from_db(String parameterValue) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM promos WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, parameterValue);
                statement.executeUpdate();
            }
        } catch (SQLException ignored) {
        }
    }

    public static void checker_deleter_promos() {
        ArrayList<Promo> promoArrayList = get_promos_from_db();
        LocalDateTime now = LocalDateTime.now();

        for (Promo promo : promoArrayList) {
            if (promo.getEnd_date().isBefore(now)) {
                delete_promo_from_db(promo.getName());
            }
        }
    }

    public static Promo get_active_promo_from_db() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM promos WHERE is_active = true";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return new Promo(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("discount"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("last_modified_date").toLocalDateTime(),
                            resultSet.getTimestamp("end_date").toLocalDateTime(),
                            resultSet.getBoolean("is_active"));
                }
            }
        } catch (SQLException ignored) {
        }
        return null;
    }
}