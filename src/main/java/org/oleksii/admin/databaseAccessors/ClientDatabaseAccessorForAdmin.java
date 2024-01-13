package org.oleksii.admin.databaseAccessors;

import org.oleksii.user.client.Client;
import org.oleksii.user.info.Address;
import org.oleksii.user.info.PaymentInfo;

import java.sql.*;

public class ClientDatabaseAccessorForAdmin extends DatabaseAccessor {
    public static Client get_client_from_db(int parameterValue) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM clients WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, parameterValue);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return new Client(
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
                }
            }
        } catch (SQLException ignored) {
        }
        return null;
    }
}
