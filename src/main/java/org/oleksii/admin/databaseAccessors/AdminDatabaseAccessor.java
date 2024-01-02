package org.oleksii.admin.databaseAccessors;

import org.mindrot.jbcrypt.BCrypt;
import org.oleksii.admin.super_users.default_administrator.Admin;

import java.sql.*;
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
                            resultSet.getString("salt"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
}
