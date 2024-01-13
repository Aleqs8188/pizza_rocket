package org.oleksii.admin.super_users.default_administrator;

import org.oleksii.enums.ConsoleColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.getAdminsFromDB;
import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.getChangesFromDB;
import static org.oleksii.admin.main.AdminMain.printSymbols;

public class AdminsList extends Admin {
    private final ArrayList<Admin> adminArrayList = getAdminsFromDB();
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void printAllAdmins() {
        System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+----------------+-------------------+---------------------+-----------------------------------------------------------------------+------------------------------------+");
        System.out.println("| ID |     Name       |       Surname     |       Username      |                              HashedPassword                           |                 Salt               |");
        System.out.println("+----+----------------+-------------------+---------------------+-----------------------------------------------------------------------+------------------------------------+");
        int counter = 1;
        for (Admin admin : adminArrayList) {
            String formattedString = String.format(
                    "|%3s | %-14s | %-17s | %-19s | %-69s | %-35s|",
                    counter,
                    admin.getName(),
                    admin.getSurname(),
                    admin.getUsername(),
                    admin.getHashedPassword(),
                    admin.getSalt());
            counter++;
            System.out.println(formattedString);
        }

        System.out.println("+----+----------------+-------------------+---------------------+-----------------------------------------------------------------------+------------------------------------+" + ConsoleColor.RESET.getCode());
    }

    public static void print_details_about_admin() throws IOException {
        printSymbols();
        System.out.print("Enter the username of the administrator whose information you want to see: ");
        String username = reader.readLine();
        printSymbols();
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*********************************************************************Information about changes*******************************************************************************");
        String[] strings = getChangesFromDB(username);
        if (strings == null) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "This administrator hasn't changed anything yet." + ConsoleColor.RESET.getCode());
            return;
        }
        System.out.printf(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "%-12s%-10s", "Date:", "Time:");
        System.out.printf("%-23s %-23s %-23s", "Reason:", "Old Value:", "New Value:");

        System.out.println(ConsoleColor.BLUE.getCode());

        for (String order : strings) {
            String[] orderInfo = order.split("#");

            int lastIndex = orderInfo.length;
            String date = orderInfo[lastIndex - 1];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime formattedDate = LocalDateTime.parse(date, formatter);
            String dateToPrint = formattedDate.getYear() + "-" + formattedDate.getMonthValue() + "-" + formattedDate.getDayOfMonth();
            String timeToPrint = formattedDate.getHour() + ":" + formattedDate.getMinute();

            System.out.printf("%-12s%-10s", dateToPrint, timeToPrint);

            for (int i = 0; i < orderInfo.length - 1; i++) {
                System.out.printf("%-24s", "'" + orderInfo[i] + "'");
            }
            System.out.println();
        }
    }
}
