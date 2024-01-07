package org.oleksii.admin.databaseAccessors;

import org.mindrot.jbcrypt.BCrypt;
import org.oleksii.admin.super_users.default_administrator.Admin;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminDatabaseAccessor extends DatabaseAccessor {

    public static int getAllIdFromDB() {
        ArrayList<Admin> adminArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM admins";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Admin admin = new Admin(
                            resultSet.getInt("id"));
                    adminArrayList.add(admin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminArrayList.size();
    }

    public static ArrayList<Admin> getAdminsFromDB() {
        ArrayList<Admin> adminArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM admins ORDER BY id ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Admin admin = new Admin(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("salt"));
                    adminArrayList.add(admin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminArrayList;
    }

    public static Admin getAdminFromDB(String parameterValue) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM admins WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, parameterValue);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return new Admin(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("salt"),
                            resultSet.getString("secret_code"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getChangesFromDB(String username) {
        String[] orders = null;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT changes FROM admins WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Array pgArray = resultSet.getArray("changes");
                        if (pgArray != null) {
                            orders = (String[]) pgArray.getArray();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static boolean addAdminToDB(Admin admin, String passwordNewAdmin) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO admins (name, surname, username, password_hash, salt) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Generation hashed password
                String salt = BCrypt.gensalt();
                String hashedPassword = BCrypt.hashpw(passwordNewAdmin, salt);
                preparedStatement.setString(1, admin.getName());
                preparedStatement.setString(2, admin.getSurname());
                preparedStatement.setString(3, admin.getUsername());
                preparedStatement.setString(4, hashedPassword);
                preparedStatement.setString(5, salt);

                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean change_username_for_admin_in_db(Admin admin, String newUsername) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE admins SET username = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, admin.getUsername());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Admin get_admin_by_secret_code(String secretCode) {
        Admin admin = null;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM admins WHERE secret_code = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, secretCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    admin = new Admin(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("salt"),
                            resultSet.getString("secret_code"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }


    public static boolean deleteAdminFromDB(String parameterValue) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM admins WHERE username = ?";
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

    public static boolean save_changes_to_db(Admin admin, String reason, String oldUsername, String newUsername) {
        LocalDateTime date = LocalDateTime.now();
        int idAdmin = admin.getId();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE admins SET changes = array_append(changes, ?) WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, reason + "#" + oldUsername + "#" + newUsername + "#" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                preparedStatement.setInt(2, idAdmin);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String get_secret_code_from_db(Admin admin) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT secret_code FROM admins WHERE username = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, admin.getUsername());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("secret_code");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving secret code from the database", e);
        }
        return null;
    }

    public static boolean changer_secret_code(Admin admin, String string) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE admins SET secret_code = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, string);
                preparedStatement.setString(2, admin.getUsername());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String searcher_the_same_secret_code(String string) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT secret_code FROM admins WHERE secret_code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, string);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("secret_code");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
