package org.oleksii.admin.databaseAccessors;

import org.oleksii.admin.promotional_code.Promo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PromoDatabaseAccessor extends DatabaseAccessor {
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
                            resultSet.getTimestamp("date_of_creation").toLocalDateTime(),
                            resultSet.getTimestamp("end_date").toLocalDateTime(),
                            resultSet.getBoolean("is_active"));
                    promoArrayList.add(promo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promoArrayList;
    }

    public static void add_promo_to_db(Promo promo) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO promos (name, discount, description, date_of_creation, end_date, is_active) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String s = createLocalDateTimeStr(promo);
                LocalDateTime a = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-M-d HH:mm"));
                preparedStatement.setString(1, promo.getName());
                preparedStatement.setInt(2, promo.getDiscount());
                preparedStatement.setString(3, promo.getDescription());
                java.sql.Timestamp timestamp_date_of_creation = java.sql.Timestamp.valueOf(a);
                preparedStatement.setTimestamp(4, timestamp_date_of_creation);
                java.sql.Timestamp timestamp_end_date = java.sql.Timestamp.valueOf(promo.getEnd_date());
                preparedStatement.setTimestamp(5, timestamp_end_date);
                preparedStatement.setBoolean(6, promo.isIs_active());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean get_promo_from_db_by_name(String parameterValue1) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM promos WHERE name = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, parameterValue1);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    new Promo(resultSet.getString("name"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete_promo_from_db(String parameterValue) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM promos WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, parameterValue);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void changer_promo_name_in_db(String parameterValue1, String parameterValue2) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE promos SET name = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, parameterValue1);
                statement.setString(2, parameterValue2);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean changer_promo_discount_in_db(String parameterValue1, String parameterValue2) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE promos SET discount = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int discount = Integer.parseInt(parameterValue1);
                statement.setInt(1, discount);
                statement.setString(2, parameterValue2);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean changer_promo_description_in_db(String parameterValue1, String parameterValue2) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE promos SET description = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, parameterValue1);
                statement.setString(2, parameterValue2);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean changer_promo_end_date_in_db(String parameterValue1, String parameterValue2) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE promos SET end_date = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                java.sql.Timestamp timestamp_end_date = java.sql.Timestamp.valueOf(LocalDateTime.parse(parameterValue1, formatter));
                statement.setTimestamp(1, timestamp_end_date);
                statement.setString(2, parameterValue2);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean changer_promo_active_in_db(String parameterValue1, String parameterValue2) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE promos SET is_active = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                boolean active = Boolean.parseBoolean(parameterValue1);
                statement.setBoolean(1, active);
                statement.setString(2, parameterValue2);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String createLocalDateTimeStr(Promo promo) {
        return promo.getDate_of_creation().getYear() +
                "-" +
                promo.getDate_of_creation().getMonthValue() +
                "-" +
                promo.getDate_of_creation().getDayOfMonth() +
                " " +
                promo.getDate_of_creation().getHour() +
                ":" +
                promo.getDate_of_creation().getMinute();
    }
}
