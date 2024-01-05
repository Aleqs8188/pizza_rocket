package org.oleksii.user.orders;

import org.oleksii.enums.ConsoleColor;
import org.oleksii.pizzas.Pizza;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static org.oleksii.api.DistanceCalculateApi.getResultOfDistance;

public class CurrentOrder {
    public static ArrayList<Pizza> order = new ArrayList<>();

    public static void printOrders() {
        int counter = 1;
        System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+----------------------+----------------------------------------------+-------+---------+------------------------------------------------------+--------------+--------+");
        System.out.println("| ID |         Name         |               Description                    | Price |  Size   |                   Ingredients                        |     Type     | Rating |");
        System.out.println("+----+----------------------+----------------------------------------------+-------+---------+------------------------------------------------------+--------------+--------+");
        for (Pizza p : order) {
            String formattedString = String.format("|%3s | %-20s | %-44s | %-5s | %-7s | %-52s | %-12s | %-6s |", counter, p.getName(), p.getDescription(), p.getPrice(), p.getSize(), p.getIngredients(), p.getType(), p.getRating());
            System.out.println(formattedString);
            counter++;
        }
        System.out.println("+----+----------------------+----------------------------------------------+-------+---------+------------------------------------------------------+--------------+--------+" + ConsoleColor.RESET.getCode());
    }


    public static double totalSum() {
        double sum = 0;
        for (Pizza p : order) {
            sum += p.getPrice();
        }
        return sum;
    }

    public static Pizza deleteObjectFromOrder(String pizzaName) {
        for (Pizza p : order) {
            if (p.getName().equals(pizzaName)) {
                order.remove(p);
                return p;
            }
        }
        return null;
    }

    public static String printOrdersName() {
        StringBuilder a = new StringBuilder();
        for (Pizza p : order) {
            a.append("'").append(p.getName()).append("'");
        }
        return a.toString();
    }

    public static double realizeAnOrder(String postalCode1, String postalCode2) {
        return getResultOfDistance(postalCode1, postalCode2);
    }

    public static String timeToDeliver(double a) {
        if (a < 1) {
            return "10 minutes";
        } else if (a < 2) {
            return "20 minutes";
        } else if (a < 3) {
            return "30 minutes";
        } else if (a < 4) {
            return "40 minutes";
        } else if (a < 5) {
            return "50 minutes";
        } else if (a < 6) {
            return "1 hour";
        } else if (a < 7) {
            return "1 hour 10 minutes";
        } else if (a < 8) {
            return "1 hour 20 minutes";
        } else if (a < 9) {
            return "1 hour 30 minutes";
        } else if (a < 10) {
            return "1 hour 40 minutes";
        } else if (a < 11) {
            return "1 hour 50 minutes";
        } else if (a < 12) {
            return "2 hours";
        } else if (a < 13) {
            return "2 hours 10 min";
        }
        return null;
    }

    public static void display_order_summary(double distance, double deliveryCost, double totalCost) {
        String distanceToPrint = timeToDeliver(distance);
        String deliveryTimeStr = "Estimated Delivery Time: " + distanceToPrint;
        String distanceStr = "Distance: " + new DecimalFormat("0.00").format(distance) + " km";
        String deliveryCostStr = "Delivery Cost: " + new DecimalFormat("0.00").format(deliveryCost) + " PLN";
        String totalCostStr = "Total Cost: " + new DecimalFormat("0.00").format(totalCost) + " PLN";

        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+------------------------------------------------------------+");
        System.out.println("|                     Your Order Details                     |");
        System.out.println("+------------------------------------------------------------+");
        System.out.printf("| %-58s |\n", deliveryTimeStr);
        System.out.printf("| %-58s |\n", distanceStr);
        System.out.printf("| %-58s |\n", deliveryCostStr);
        System.out.printf("| %-58s |\n", totalCostStr);
        System.out.println("+------------------------------------------------------------+");
        System.out.println("|               Thank You for Your Order.                    |");
        System.out.println("+------------------------------------------------------------+" + ConsoleColor.RESET.getCode());
    }
}
