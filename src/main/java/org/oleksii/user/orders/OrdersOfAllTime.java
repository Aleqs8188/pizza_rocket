package org.oleksii.user.orders;

import org.oleksii.user.client.Client;

import static org.oleksii.user.databaseAccessors.ClientDatabaseAccessor.getOrdersFromDB;

public class OrdersOfAllTime extends Client {
    public String[] clientOrders;

    public OrdersOfAllTime(String[] strings) {
        clientOrders = strings;
    }

    //rework
    public static void printAllOrders(String[] strings) {
        for (String order : strings) {
            String[] orderInfo = order.split("!");
            for (String s : orderInfo) {
                System.out.print(s + " ");
                //System.out.printf("| %20s | %20s | %20s | %20s", pizzaOrder1, pizzaOrder2, pizzaOrder3, orderDate);
            }
            System.out.println();
        }
    }

    //testing
    public static void main(String[] args) {
        Client client = new Client();
        client.setId(2);
        printAllOrders(getOrdersFromDB(client));
    }
}
