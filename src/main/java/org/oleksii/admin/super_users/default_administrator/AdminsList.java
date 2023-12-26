package org.oleksii.admin.super_users.default_administrator;

import org.oleksii.enums.ConsoleColor;

import java.util.ArrayList;

import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.getAdminsFromDB;

public class AdminsList extends Admin {
    private ArrayList<Admin> adminArrayList = getAdminsFromDB();

    public void printAllAdmins() {
        System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+----------------------+---------------------------+----------------------------+------------------------------------------------------------------------------+--------------------------------------+");
        System.out.println("| ID |        Name          |          Surname          |          Username          |                                HashedPassword                                |                 Salt                 |");
        System.out.println("+----+----------------------+---------------------------+----------------------------+------------------------------------------------------------------------------+--------------------------------------+");
        int counter = 1;
        for (Admin admin : adminArrayList) {
            StringBuilder formattedString = new StringBuilder(String.format(
                    "|%3s | %-20s | %-25s | %-26s | %-76s | %-37s|",
                    counter,
                    admin.getName(),
                    admin.getSurname(),
                    admin.getUsername(),
                    admin.getHashedPassword(),
                    admin.getSalt()));
            counter++;
            System.out.println(formattedString);
        }

        System.out.println("+----+----------------------+---------------------------+----------------------------+------------------------------------------------------------------------------+--------------------------------------+" + ConsoleColor.RESET.getCode());
    }
}
