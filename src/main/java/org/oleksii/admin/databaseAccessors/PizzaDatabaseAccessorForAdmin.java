package org.oleksii.admin.databaseAccessors;

import org.oleksii.pizzas.Pizza;

import java.sql.*;

public class PizzaDatabaseAccessorForAdmin extends DatabaseAccessor {
    public static Pizza get_pizza_from_db_by_name(String parameterValue1) {
        Pizza myObject = null;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM pizzas WHERE name = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, parameterValue1);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    myObject = new Pizza(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getDouble("price"),
                            resultSet.getString("size"),
                            resultSet.getString("ingredients"),
                            resultSet.getString("type"),
                            resultSet.getString("rating"));
                }
            }
        } catch (SQLException ignored) {
        }
        return myObject;
    }

    public static void add_pizza_to_db(Pizza pizza) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO pizzas (name, description, price, size, ingredients, type, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, pizza.getName());
                preparedStatement.setString(2, pizza.getDescription());
                preparedStatement.setDouble(3, pizza.getPrice());
                preparedStatement.setString(4, pizza.getSize());
                preparedStatement.setString(5, pizza.getIngredients());
                preparedStatement.setString(6, pizza.getType());
                preparedStatement.setString(7, pizza.getRating());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ignored) {
        }
    }

    public static boolean delete_pizza_from_db(String parameterValue) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM pizzas WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, parameterValue);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException ignored) {
        }
        return false;
    }
}
