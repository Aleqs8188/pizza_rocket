package org.oleksii.user.orders;

import org.oleksii.enums.ConsoleColor;
import org.oleksii.user.client.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrdersOfAllTime extends Client {
    public String[] clientOrders;

    public OrdersOfAllTime(String[] strings) {
        clientOrders = strings;
    }

    public static void printAllOrders(String[] strings) {
        System.out.printf(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "%-12s%-10s", "Date:", "Time:");

        for (int i = 1; i <= getMaxItems(strings); i++) {
            if (i == 6) {
                break;
            }
            System.out.printf("%-25s", "Pizza: " + i);
        }

        System.out.println(ConsoleColor.BLUE.getCode());

        for (String order : strings) {
            String[] orderInfo = order.split("!");

            int lastIndex = orderInfo.length;
            String date = orderInfo[lastIndex - 1];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime formattedDate = LocalDateTime.parse(date, formatter);
            String dateToPrint = formattedDate.getYear() + "-" + formattedDate.getMonthValue() + "-" + formattedDate.getDayOfMonth();
            String timeToPrint = formattedDate.getHour() + ":" + formattedDate.getMinute();

            System.out.printf("%-12s%-10s", dateToPrint, timeToPrint);

            for (int i = 0; i < orderInfo.length - 1; i++) {
                if (i == 5) {
                    System.out.print("\n                      ");
                }
                System.out.printf("%-25s", "'" + orderInfo[i] + "'");
            }

            System.out.println();
        }
    }

    private static int getMaxItems(String[] orders) {
        int maxItems = 0;
        for (String order : orders) {
            String[] orderInfo = order.split("!");
            maxItems = Math.max(maxItems, orderInfo.length - 1);
        }
        return maxItems;
    }
}
