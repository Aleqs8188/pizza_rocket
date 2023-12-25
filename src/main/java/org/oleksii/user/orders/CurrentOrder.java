package org.oleksii.user.orders;

import org.oleksii.pizzas.Pizza;

import java.util.ArrayList;

import static org.oleksii.api.DistanceCalculateApi.getResultOfDistance;

public class CurrentOrder {
    public static ArrayList<Pizza> order = new ArrayList<>();
//Вивод
    public static void printOrders() {
        System.out.println("+----+----------------------+----------------------------------------------------+------------+------------+--------------------------------------------------------------+-----------------+-----------------+");
        System.out.println("| ID |         Name         |                  Description                       |   Price    |   Size     |                      Ingredients                             |      Type       |     Rating      |");
        System.out.println("+----+----------------------+----------------------------------------------------+------------+------------+--------------------------------------------------------------+-----------------+-----------------+");

        for (Pizza p : order) {
            StringBuilder formattedString = new StringBuilder(String.format(
                    "|%3s | %-20s | %-50s | %-10s | %-10s | %-60s | %-15s | %-15s |",
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getSize(),
                    p.getIngredients(),
                    p.getType(),
                    p.getRating()));

            System.out.println(formattedString);
        }

        System.out.println("+----+----------------------+----------------------------------------------------+------------+------------+--------------------------------------------------------------+-----------------+-----------------+");
    }



    public static double totalSum() {
        double sum = 0;
        for (Pizza p : order) {
            sum += p.getPrice();
        }
        return sum;
    }

    public static Pizza deleteObjectFromOrder(int idPizza) {
        for (Pizza p : order) {
            if (p.getId() == idPizza) {
                order.remove(p);
                return p;
            }
        }
        return null;
    }

    public static String printOrdersID() {
        StringBuilder a = new StringBuilder();
        for (Pizza p : order) {
            a.append("IdPizza:").append(p.getId()).append(", ").append("name=").append(p.getName()).append(", ");
        }
        return a.toString();
    }

    public static double realizeAnOrder(String postalCode1, String postalCode2) {
        double a = getResultOfDistance(postalCode1, postalCode2);
        if (a > 0) {
            timeToDeliver(a);
            return a;
        }
        return a;
    }

    public static int timeToDeliver(double a) {
        if (a < 1) {
            System.out.println("*Your order will be delivered within 10 minutes");
            return 0;
        } else if (a < 2) {
            System.out.println("*Your order will be delivered within 20 minutes");
            return 4;
        } else if (a < 3) {
            System.out.println("*Your order will be delivered within 30 minutes");
            return 6;
        } else if (a < 4) {
            System.out.println("*Your order will be delivered within 40 minutes");
            return 8;
        } else if (a < 5) {
            System.out.println("*Your order will be delivered within 50 minutes");
            return 10;
        } else if (a < 6) {
            System.out.println("*Your order will be delivered within 1 hour");
            return 12;
        } else if (a < 7) {
            System.out.println("*Your order will be delivered within 1 hour 10 minutes");
            return 14;
        } else if (a < 8) {
            System.out.println("*Your order will be delivered within 1 hour 20 minutes");
            return 16;
        } else if (a < 9) {
            System.out.println("*Your order will be delivered within 1 hour 30 minutes");
            return 18;
        } else if (a < 10) {
            System.out.println("*Your order will be delivered within 1 hour 40 minutes");
            return 20;
        } else if (a < 11) {
            System.out.println("*Your order will be delivered within 1 hour 50 minutes");
            return 22;
        } else if (a < 12) {
            System.out.println("*Your order will be delivered within 2 hours");
            return 24;
        } else if (a < 13) {
            System.out.println("*Your order will be delivered within more than 2 hours");
            return 26;
        }
        return 0;
    }
}
