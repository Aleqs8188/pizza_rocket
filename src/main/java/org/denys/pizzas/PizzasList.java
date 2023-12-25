package org.denys.pizzas;

import org.denys.user.databaseAccessors.PizzaDatabaseAccessor;

import java.util.ArrayList;

public class PizzasList extends Pizza {
    static ArrayList<Pizza> pizzaArrayList = PizzaDatabaseAccessor.getPizzasFromBD();
    // Тут вивод
    public static void printPizzas() {
        System.out.println("+----+----------------------+----------------------------------------------------+------------+------------+--------------------------------------------------------------+-----------------+-----------------+");
        System.out.println("| ID |         Name         |                  Description                       |   Price    |   Size     |                      Ingredients                             |      Type       |     Rating      |");
        System.out.println("+----+----------------------+----------------------------------------------------+------------+------------+--------------------------------------------------------------+-----------------+-----------------+");

        for (Pizza p : pizzaArrayList) {
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

    public static void main(String[] args) {
        printPizzas();
    }

}
