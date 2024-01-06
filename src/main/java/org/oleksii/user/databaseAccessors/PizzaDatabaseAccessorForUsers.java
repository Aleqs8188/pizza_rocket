package org.oleksii.user.databaseAccessors;

import org.oleksii.pizzas.Pizza;

import java.sql.*;
import java.util.ArrayList;

public class PizzaDatabaseAccessorForUsers extends DatabaseAccessor {

    public static ArrayList<Pizza> getPizzasFromBD() {
        ArrayList<Pizza> pizzaArrayList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM pizzas ORDER BY id ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Pizza pizza = new Pizza(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getDouble("price"),
                            resultSet.getString("size"),
                            resultSet.getString("ingredients"),
                            resultSet.getString("type"),
                            resultSet.getString("rating"));
                    pizzaArrayList.add(pizza);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pizzaArrayList;
    }

    public static Pizza getPizzaFromBDByParameters(String parameterValue1) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myObject;
    }
}
