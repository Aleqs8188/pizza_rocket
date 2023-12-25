package org.oleksii.user.databaseAccessors;

import org.oleksii.user.info.Address;
import org.oleksii.user.client.Client;
import org.oleksii.user.info.PaymentInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDatabaseAccessor extends DatabaseAccessor {
    public static ArrayList<Client> getClientsFromBD() {
        ArrayList<Client> clientArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM clients";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Client client = new Client(
                            resultSet.getInt("id"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"),
                            resultSet.getString("phonenumber"),
                            resultSet.getString("email"), new Address(
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postalcode")), new PaymentInfo(
                            resultSet.getString("creditcardnumber"), new Address(
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postalcode"))));
                    clientArrayList.add(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientArrayList;
    }

    public static boolean addClientToBD(Client client) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO clients (id, firstname, lastname, phonenumber, email, street, city, postalcode, creditcardnumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, client.getId());
                preparedStatement.setString(2, client.getFirstName());
                preparedStatement.setString(3, client.getLastName());
                preparedStatement.setString(4, client.getPhoneNumber());
                preparedStatement.setString(5, client.getEmail());
                preparedStatement.setString(6, client.getAddress().getStreet());
                preparedStatement.setString(7, client.getAddress().getCity());
                preparedStatement.setString(8, client.getAddress().getPostalCode());
                preparedStatement.setString(9, client.getPaymentInfo().getCreditCardNumber());

                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Client searchClientInBD(String parameterValue1, String parameterValue2, String parameterValue3) {
        List<Client> resultList = new ArrayList<>();
        Client myObject = new Client();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM clients WHERE email = ? and phonenumber = ? and creditcardnumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, parameterValue1);
                preparedStatement.setString(2, parameterValue2);
                preparedStatement.setString(3, parameterValue3);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    myObject = new Client(resultSet.getInt("id"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"),
                            resultSet.getString("phonenumber"),
                            resultSet.getString("email"), new Address(
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postalcode")), new PaymentInfo(
                            resultSet.getString("creditcardnumber"), new Address(
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postalcode"))));
                    resultList.add(myObject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return myObject;
    }

    public static Client searchClientInBD(String parameterValue1, String parameterValue2) {
        List<Client> resultList = new ArrayList<>();
        Client myObject = new Client();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM clients WHERE email = ? OR phonenumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, parameterValue1);
                preparedStatement.setString(2, parameterValue2);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    myObject = new Client(resultSet.getInt("id"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"),
                            resultSet.getString("phonenumber"),
                            resultSet.getString("email"), new Address(
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postalcode")), new PaymentInfo(
                            resultSet.getString("creditcardnumber"), new Address(
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postalcode"))));
                    resultList.add(myObject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myObject;
    }

    // Тут запрос новий
    public static String[][] getOrdersFromDB(Client client) {
        int idClient = client.getId();
        String[][] orders = null;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT orders FROM clients WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idClient);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Array pgArray = resultSet.getArray("orders");
                        if (pgArray != null) {
                            Object array = pgArray.getArray();
                            orders = (String[][]) array;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
