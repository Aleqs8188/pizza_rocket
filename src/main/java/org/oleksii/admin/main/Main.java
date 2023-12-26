package org.oleksii.admin.main;

import org.oleksii.admin.super_users.chief_administrator.SuperAdmin;
import org.oleksii.admin.super_users.default_administrator.Admin;
import org.mindrot.jbcrypt.BCrypt;
import org.oleksii.admin.super_users.default_administrator.AdminsList;
import org.oleksii.enums.ConsoleColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.*;
import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.getAdminFromDB;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            int choice;
            printSymbols();
            System.out.print("(1) ADMIN ||| (2) CHIEF ADMIN ---| ");
            choice = scanner.nextInt();
            if (choice > 2) {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                continue;
            }
            switch (choice) {
                case 1:
                    firstCaseLoop:
                    while (true) {
                        printSymbols();
                        Admin adminLog;
                        System.out.println(ConsoleColor.BOLD.getCode() + "|||||||||||LOGIN TO ADMIN||||||||||||" + ConsoleColor.RED.getCode() + " <to back - enter (9)>" + ConsoleColor.RESET.getCode());
                        int counter1 = 1;
                        while (true) {
                            System.out.print(ConsoleColor.BOLD.getCode() + "Username: " + ConsoleColor.RESET.getCode());
                            String usernameLogin = reader.readLine();
                            if (usernameLogin.equals("9")) {
                                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                                break firstCaseLoop;
                            }
                            adminLog = getAdminFromDB(usernameLogin);
                            if (adminLog != null) {
                                break;
                            } else {
                                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There is no administrator with this username..." + ConsoleColor.RESET.getCode());
                                if (counter1 >= 5) {
                                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "If you do not remember your 'username', contact the main administrator..." + ConsoleColor.RESET.getCode());
                                    break firstCaseLoop;
                                }
                            }
                            counter1++;
                        }
                        int counter_2 = 0;
                        while (true) {
                            System.out.print(ConsoleColor.BOLD.getCode() + "Password: " + ConsoleColor.RESET.getCode());
                            String passwordLogin = reader.readLine();
                            if (passwordLogin.equals("9")) {
                                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                                break firstCaseLoop;
                            }
                            if (BCrypt.checkpw(passwordLogin, adminLog.getHashedPassword())) {
                                break;
                            } else {
                                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "The password is not correct..." + ConsoleColor.RESET.getCode());
                                if (counter_2 >= 5) {
                                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "If you do not remember your 'password', contact the main administrator..." + ConsoleColor.RESET.getCode());
                                    break firstCaseLoop;
                                }
                            }
                            counter_2++;
                        }
                    }
                    break;
                case 2:
                    int counter_1 = 0;
                    secondCaseLoop:
                    while (true) {
                        printSymbols();
                        SuperAdmin superAdmin = new SuperAdmin();
                        System.out.println(ConsoleColor.BOLD.getCode() + "|||||||||||LOGIN TO CHIEF ADMIN||||||||||||" + ConsoleColor.RED.getCode() + " <to back - enter (9)>" + ConsoleColor.RESET.getCode());
                        String usernameChiefAdmin;
                        while (true) {
                            System.out.print(ConsoleColor.BOLD.getCode() + "Username: " + ConsoleColor.RESET.getCode());
                            usernameChiefAdmin = reader.readLine();
                            if (usernameChiefAdmin.equals("9")) {
                                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                                break secondCaseLoop;
                            }
                            if (superAdmin.getUSER_NAME().equals(usernameChiefAdmin)) {
                                break;
                            } else {
                                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "The 'username' is not correct..." + ConsoleColor.RESET.getCode());
                                if (counter_1 >= 5) {
                                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Are you really sick? Have you really forgotten your username?" + ConsoleColor.RESET.getCode());
                                    break secondCaseLoop;
                                }
                            }
                            counter_1++;
                        }
                        int counter_2 = 0;
                        String passwordChiefAdmin;
                        while (true) {
                            System.out.print(ConsoleColor.BOLD.getCode() + "Password: " + ConsoleColor.RESET.getCode());
                            passwordChiefAdmin = reader.readLine();
                            if (passwordChiefAdmin.equals("9")) {
                                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                                break secondCaseLoop;
                            }
                            if (superAdmin.getPASSWORD().equals(passwordChiefAdmin)) {
                                break;
                            } else {
                                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "The 'username' is not correct..." + ConsoleColor.RESET.getCode());
                                if (counter_2 >= 5) {
                                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Are you really sick? Have you really forgotten your password?" + ConsoleColor.RESET.getCode());
                                    break secondCaseLoop;
                                }
                            }
                            counter_2++;
                        }
                        while (true) {
                            printSymbols();
                            System.out.print("(1) Managing Administrators || (2) Managing Pizzas || (3) Managing Promos || " + ConsoleColor.RED.getCode() + "(4) Exit ---| " + ConsoleColor.RESET.getCode());
                            choice = scanner.nextInt();
                            if (choice > 4 /* rewrite */) {
                                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                                continue;
                            } else if (choice == 4) {
                                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Exit" + ConsoleColor.RESET.getCode());
                                printSymbols();
                                return;
                            }
                            switch (choice) {
                                case 1:
                                    while (true) {
                                        AdminsList admins = new AdminsList();
                                        printSymbols();
                                        admins.printAllAdmins();
                                        printSymbols();
                                        System.out.print("(1) Add new administrator || (2) Delete administrator || " + ConsoleColor.RED.getCode() + "(3) Back ---| " + ConsoleColor.RESET.getCode());
                                        choice = scanner.nextInt();
                                        if (choice > 3 /* rewrite */) {
                                            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                                        } else if (choice == 3) {
                                            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                                            break;
                                        }
                                        switch (choice) {
                                            case 1:
                                                printSymbols();
                                                int idNewAdmin = getAllIdFromDB();
                                                System.out.print(ConsoleColor.BOLD.getCode() + "Name: " + ConsoleColor.RESET.getCode());
                                                String nameNewAdmin = reader.readLine();
                                                System.out.print(ConsoleColor.BOLD.getCode() + "Surname: " + ConsoleColor.RESET.getCode());
                                                String surNameNewAdmin = reader.readLine();
                                                System.out.print(ConsoleColor.BOLD.getCode() + "Username: " + ConsoleColor.RESET.getCode());
                                                String userNameNewAdmin = reader.readLine();
                                                if (getAdminFromDB(userNameNewAdmin) != null) {
                                                    System.out.println(ConsoleColor.RED.getCode() + "*An administrator with the same 'username' already exists, select a new username");
                                                    System.out.println("*Try again..." + ConsoleColor.RESET.getCode());
                                                    continue;
                                                }
                                                System.out.print(ConsoleColor.BOLD.getCode() + "Password: " + ConsoleColor.RESET.getCode());
                                                String passwordNewAdmin = reader.readLine();
                                                addAdminToDB(new Admin(idNewAdmin + 1, nameNewAdmin, surNameNewAdmin, userNameNewAdmin), passwordNewAdmin);
                                                printSymbols();
                                                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*New admin added*" + ConsoleColor.RESET.getCode());
                                                break;
                                            case 2:
                                                // Rework
                                                printSymbols();
                                                System.out.print("Enter username: ");
                                                String username = reader.readLine();
                                                deleteAdminFromDB(username);
                                                break;
                                        }
                                    }
                                    continue;
                                case 2:
                                    printSymbols();
                                    // Выводим все пиццы
                                    System.out.print("(1) Add new pizza || (2) Delete pizza ---| ");
                                    choice = scanner.nextInt();
                                    if (choice > 2 /* rewrite 2*/) {
                                        System.err.println("Incorrect value, try again...");
                                        continue;
                                    }
                                    switch (choice) {
                                        case 1:
                                            // Add pizza
                                            break;
                                        case 2:
                                            // Delete pizza
                                            break;
                                    }
                                    break;
                                case 3:
                                    printSymbols();
                                    //Выводим все промоакции
                                    System.out.print("(1) Add new promo || (2) Delete promo ---| ");
                                    choice = scanner.nextInt();
                                    if (choice > 2 /* rewrite 2*/) {
                                        System.err.println("Incorrect value, try again...");
                                        continue;
                                    }
                                    switch (choice) {
                                        case 1:
                                            // Add promo
                                            break;
                                        case 2:
                                            // Delete promo
                                            break;
                                    }
                                    break;
                            }
                        }
                    }
            }
        }
    }

    public static void printSymbols() {
        System.out.println(ConsoleColor.BLUE.getCode() + "************************************************************************************************************************************************************************************************************" + ConsoleColor.RESET.getCode());
    }
}


