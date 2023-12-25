package org.denys.admin.databaseAccessors;

import org.denys.admin.super_users.default_administrator.Admin;
import org.denys.user.client.Client;
import org.denys.user.info.Address;
import org.denys.user.info.PaymentInfo;

import java.sql.*;
import java.util.ArrayList;

public class AdminDatabaseAccessor extends DatabaseAccessor {

    public static ArrayList<Admin> getAdminsFromDB() {
        ArrayList<Admin> adminArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM admins";
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

    public static boolean addAdminToDB(Admin admin) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO admins (id, name, surname, username, password_hash, salt) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, admin.getId());
                preparedStatement.setString(2, admin.getName());
                preparedStatement.setString(3, admin.getSurname());
                preparedStatement.setString(4, admin.getUsername());
                preparedStatement.setString(5, admin.getHashedPassword());
                preparedStatement.setString(6, admin.getSalt());

                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
