package org.oleksii.user.orders;

import org.oleksii.user.client.Client;

import java.util.Arrays;

public class OrdersOfAllTime extends Client {
    public String[][] orders;

    public OrdersOfAllTime(String[][] orders) {
        this.orders = orders;
    }

    public String[][] getOrders() {
        return orders;
    }

    public void setOrders(String[][] orders) {
        this.orders = orders;
    }

    public void printAllOrders() {
        for (String[] strings : orders) {
            System.out.println(Arrays.asList(strings));
        }
    }
}
