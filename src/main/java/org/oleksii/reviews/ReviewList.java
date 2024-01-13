package org.oleksii.reviews;

import org.oleksii.enums.ConsoleColor;
import org.oleksii.user.client.Client;

import java.util.ArrayList;

import static org.oleksii.admin.databaseAccessors.ClientDatabaseAccessorForAdmin.get_client_from_db;
import static org.oleksii.user.main.UserMain.printSymbols;

public class ReviewList extends Review {
    public ArrayList<Review> reviewArrayList;

    public ReviewList(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

    public void print_reviews() {
        int counter = 1;
        if (reviewArrayList.isEmpty()) {
            printSymbols();
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There are no reviews here yet" + ConsoleColor.RESET.getCode());
            return;
        }

        System.out.println(ConsoleColor.BLUE.getCode() + ConsoleColor.BOLD.getCode() + "***********************************************************************Reviews for all time***********************************************************************");
        System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+-------------+--------+--------+-------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("| ID |    Date     |  Time  | Rating |                                                  Description                                                                  |");
        System.out.println("+----+-------------+--------+--------+-------------------------------------------------------------------------------------------------------------------------------+");
        for (Review review : reviewArrayList) {
            String date = review.getDateTime().getYear() + "-" + review.getDateTime().getMonthValue() + "-" + review.getDateTime().getDayOfMonth();
            String time = review.getDateTime().getHour() + ":" + review.getDateTime().getMinute();
            String formattedString = String.format("|%3s | %-11s | %-6s | %-6s | %-125s |", counter, date, time, review.getRating(), review.getDescription());
            System.out.println(formattedString);
            counter++;
        }
        System.out.println("+----+-------------+--------+--------+-------------------------------------------------------------------------------------------------------------------------------+" + ConsoleColor.RESET.getCode());
    }

    public void print_reviews_for_admin() {
        int counter = 1;
        if (reviewArrayList.isEmpty()) {
            printSymbols();
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There are no reviews here yet" + ConsoleColor.RESET.getCode());
            return;
        }
        System.out.println(ConsoleColor.BLUE.getCode() + ConsoleColor.BOLD.getCode() + "*************************************************************************Reviews for all time*************************************************************************");
        System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+-------------+--------+--------+-------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("| ID |    Date     |  Time  | Rating |                                                  Description                                                                  |");
        System.out.println("+----+-------------+--------+--------+-------------------------------------------------------------------------------------------------------------------------------+");
        for (Review review : reviewArrayList) {
            Client client = get_client_from_db(review.getClient_id());
            if (client == null) {
                continue;
            }
            String date = review.getDateTime().getYear() + "-" + review.getDateTime().getMonthValue() + "-" + review.getDateTime().getDayOfMonth();
            String time = review.getDateTime().getHour() + ":" + review.getDateTime().getMinute();
            String formattedString = String.format("|%3s | %-11s | %-6s | %-6s | %-125s |", counter, date, time, review.getRating(), review.getDescription());
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + formattedString);
            counter++;
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+--------------------------------------------------------------------------------------------------------------------------------------------------------------------+" + ConsoleColor.RESET.getCode());
            String formattedString2 = String.format("| %-25s | %-40s | %-20s | %-12s | %-20s | %-28s |", client.getFirstName() + " " + client.getLastName(), "Email: " + client.getEmail(), "Phone: " + client.getPhoneNumber(), "P-Code: " + client.getAddress().getPostalCode(), "City: " + client.getAddress().getCity(), "Street: " + client.getAddress().getStreet());
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + formattedString2);
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+-------------+--------+--------+-------------------------------------------------------------------------------------------------------------------------------+" + ConsoleColor.RESET.getCode());
        }
    }
}
