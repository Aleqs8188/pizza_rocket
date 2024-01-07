package org.oleksii.admin.databaseAccessors;

import org.oleksii.admin.promotional_code_for_admin.Promo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class PromoDatabaseAccessorForAdmin extends DatabaseAccessor {
    public static ArrayList<Promo> get_promos_from_db() {
        ArrayList<Promo> promoArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM promos ORDER BY is_active DESC";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promoArrayList;
    }

    public static void add_promo_to_db(Promo promo) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO promos (name, discount, description, last_modified_date, end_date, is_active) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, promo.getName());
                preparedStatement.setInt(2, promo.getDiscount());
                preparedStatement.setString(3, promo.getDescription());
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime truncatedDateTime = now.truncatedTo(ChronoUnit.MINUTES);
                java.sql.Timestamp timestampDate = java.sql.Timestamp.valueOf(truncatedDateTime);
                preparedStatement.setTimestamp(4, timestampDate);
                java.sql.Timestamp timestamp_end_date = java.sql.Timestamp.valueOf(promo.getEnd_date());
                preparedStatement.setTimestamp(5, timestamp_end_date);
                preparedStatement.setBoolean(6, promo.isIs_active());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void creator_last_modified_date(String parameterValue1) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE promos SET last_modified_date = ? WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime truncatedDateTime = now.truncatedTo(ChronoUnit.MINUTES);
                java.sql.Timestamp timestampDate = java.sql.Timestamp.valueOf(truncatedDateTime);
                preparedStatement.setTimestamp(1, timestampDate);
                preparedStatement.setString(2, parameterValue1);
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
        creator_last_modified_date(parameterValue);
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
        creator_last_modified_date(parameterValue2);
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
        creator_last_modified_date(parameterValue2);
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
        creator_last_modified_date(parameterValue2);
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
        creator_last_modified_date(parameterValue2);
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
        creator_last_modified_date(parameterValue2);
        change_all_promo_active_on_false();
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

    public static void change_all_promo_active_on_false() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE promos SET is_active = false WHERE is_active = true";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
