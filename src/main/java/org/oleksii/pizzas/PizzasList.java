package org.oleksii.pizzas;

import org.oleksii.enums.ConsoleColor;
import org.oleksii.user.databaseAccessors.PizzaDatabaseAccessorForUsers;

import java.util.ArrayList;

public class PizzasList extends Pizza {
    ArrayList<Pizza> pizzaArrayList = PizzaDatabaseAccessorForUsers.getPizzasFromBD();

    public void print_pizzas() {
        int counter = 1;
        System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+----------------------+----------------------------------------------+-------+---------+------------------------------------------------------+--------------+--------+");
        System.out.println("| ID |         Name         |               Description                    | Price |  Size   |                   Ingredients                        |     Type     | Rating |");
        System.out.println("+----+----------------------+----------------------------------------------+-------+---------+------------------------------------------------------+--------------+--------+");
        for (Pizza p : pizzaArrayList) {
            StringBuilder formattedString = new StringBuilder(String.format(
                    "|%3s | %-20s | %-44s | %-5s | %-7s | %-52s | %-12s | %-6s |",
                    counter,
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getSize(),
                    p.getIngredients(),
                    p.getType(),
                    p.getRating()));
            System.out.println(formattedString);
            counter++;
        }

        System.out.println("+----+----------------------+----------------------------------------------+-------+---------+------------------------------------------------------+--------------+--------+" + ConsoleColor.RESET.getCode());
    }
}
