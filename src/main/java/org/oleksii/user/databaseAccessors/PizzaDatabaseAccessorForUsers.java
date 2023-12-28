package org.oleksii.user.databaseAccessors;

import org.oleksii.user.client.Client;
import org.oleksii.pizzas.Pizza;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.oleksii.user.orders.CurrentOrder.printOrdersID;

public class PizzaDatabaseAccessorForUsers extends DatabaseAccessor {

    public static ArrayList<Pizza> getPizzasFromBD() {
        ArrayList<Pizza> pizzaArrayList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM pizzas";
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

    public static Pizza getPizzaFromBDByParameters(int parameterValue1) {
        Pizza myObject = null;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM pizzas WHERE id = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, parameterValue1);

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

    public static boolean addAnOrderToDB(Client client) {
        Date date = new Date();
        int idClient = client.getId();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql1 = "UPDATE clients SET orders = orders || ARRAY[?]::character varying[] WHERE id = ?";
            String[] dataArray = {printOrdersID(), date.toString()};
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql1)) {
                java.sql.Array pgArray = connection.createArrayOf("varchar", dataArray);
                preparedStatement.setArray(1, pgArray);
                preparedStatement.setInt(2, idClient);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
