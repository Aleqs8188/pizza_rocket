package org.oleksii.reviews;

import org.oleksii.enums.ConsoleColor;

import java.util.ArrayList;

import static org.oleksii.user.main.Main.printSymbols;

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
        System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+----------+-------+--------+-------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("| ID |   Date   | Time  | Rating |                                                  Description                                                                  |");
        System.out.println("+----+----------+-------+--------+-------------------------------------------------------------------------------------------------------------------------------+");
        for (Review review : reviewArrayList) {
            String date = review.getDateTime().getYear() + "-" + review.getDateTime().getMonthValue() + "-" + review.getDateTime().getDayOfMonth();
            String time = review.getDateTime().getHour() + ":" + review.getDateTime().getMinute();
            String formattedString = String.format("|%3s | %-8s | %-5s | %-6s | %-125s |", counter, date, time, review.getRating(), review.getDescription());
            System.out.println(formattedString);
            counter++;
        }
        System.out.println("+----+----------+-------+--------+-------------------------------------------------------------------------------------------------------------------------------+" + ConsoleColor.RESET.getCode());
    }
}
