package org.oleksii.admin.main;

import org.oleksii.admin.super_users.chief_administrator.SuperAdmin;
import org.oleksii.admin.super_users.default_administrator.Admin;
import org.mindrot.jbcrypt.BCrypt;
import org.oleksii.admin.super_users.default_administrator.AdminsList;
import org.oleksii.enums.ConsoleColor;
import org.oleksii.pizzas.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.*;
import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.getAdminFromDB;
import static org.oleksii.admin.databaseAccessors.PizzaDatabaseAccessorForAdmins.add_pizza_to_db;
import static org.oleksii.admin.databaseAccessors.PizzaDatabaseAccessorForAdmins.get_pizza_from_db_by_name;
import static org.oleksii.pizzas.PizzasList.print_pizzas;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static int choice;

    public static void main(String[] args) throws IOException {
        while (true) {
            switch (start_login()) {
                case 1:
                    login_to_admin();
                    break;
                case 2:
                    login_to_chief_admin();
                    break;
                case 3:
                    return;
            }
        }
    }

    public static void login_to_admin() throws IOException {
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
            int counter_2 = 1;
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
    }

    public static void login_to_chief_admin() throws IOException {
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
                switch (start_managing()) {
                    case 1:
                        main_chief_administrator();
                        break;
                    case 2:
                        main_pizza();
                        break;
                    case 3:
                        main_promo();
                        break;
                    case 4:
                        return;
                }
            }
        }
    }

    public static void main_chief_administrator() throws IOException {
        while (true) {
            switch (start_managing_admins()) {
                case 1:
                    add_new_administrator();
                    break;
                case 2:
                    delete_administrator();
                    break;
                case 3:
                    return;
            }
        }
    }

    public static void add_new_administrator() throws IOException {
        printSymbols();
        int idNewAdmin = getAllIdFromDB();
        String userNameNewAdmin;
        System.out.print(ConsoleColor.BOLD.getCode() + "Name: " + ConsoleColor.RESET.getCode());
        String nameNewAdmin = reader.readLine();
        System.out.print(ConsoleColor.BOLD.getCode() + "Surname: " + ConsoleColor.RESET.getCode());
        String surNameNewAdmin = reader.readLine();
        while (true) {
            System.out.print(ConsoleColor.BOLD.getCode() + "Username: " + ConsoleColor.RESET.getCode());
            userNameNewAdmin = reader.readLine();
            if (getAdminFromDB(userNameNewAdmin) != null) {
                System.out.println(ConsoleColor.RED.getCode() + "*An administrator with the same 'username' already exists, select a new username");
                System.out.println("*Try again..." + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        System.out.print(ConsoleColor.BOLD.getCode() + "Password: " + ConsoleColor.RESET.getCode());
        String passwordNewAdmin = reader.readLine();
        addAdminToDB(new Admin(idNewAdmin + 1, nameNewAdmin, surNameNewAdmin, userNameNewAdmin), passwordNewAdmin);
        printSymbols();
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*New admin added*" + ConsoleColor.RESET.getCode());
    }

    public static void delete_administrator() throws IOException {
        // Rework
        printSymbols();
        System.out.print("Enter username: ");
        String username = reader.readLine();
        deleteAdminFromDB(username);
    }

    public static void main_pizza() throws IOException {
        while (true) {
            switch (start_managing_pizza()) {
                case 1:
                    add_new_pizza();
                    break;
                case 2:
                    delete_pizza();
                    break;
                case 3:
                    return;
            }
        }
    }

    public static void add_new_pizza() throws IOException { // In console error id key, try to add it 20 times
        printSymbols();
        String name;
        while (true) {
            System.out.print(ConsoleColor.BOLD.getCode() + "Name: " + ConsoleColor.RESET.getCode());
            name = reader.readLine();
            if (get_pizza_from_db_by_name(name) != null) {
                System.out.println(ConsoleColor.RED.getCode() + "*An Pizza with name '" + name + "' already exists");
                System.out.println("*Choose new name for pizza" + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        System.out.print(ConsoleColor.BOLD.getCode() + "Description: " + ConsoleColor.RESET.getCode());
        String description = reader.readLine();
        System.out.print(ConsoleColor.BOLD.getCode() + "Price: " + ConsoleColor.RESET.getCode());
        String price = reader.readLine();
        System.out.print(ConsoleColor.BOLD.getCode() + "Size: " + ConsoleColor.RESET.getCode());
        String size = reader.readLine();
        System.out.print(ConsoleColor.BOLD.getCode() + "Ingredients: " + ConsoleColor.RESET.getCode());
        String ingredients = reader.readLine();
        System.out.print(ConsoleColor.BOLD.getCode() + "Type: " + ConsoleColor.RESET.getCode());
        String type = reader.readLine();
        System.out.print(ConsoleColor.BOLD.getCode() + "Rating: " + ConsoleColor.RESET.getCode());
        String rating = reader.readLine();
        add_pizza_to_db(new Pizza(name, description, Double.parseDouble(price), size, ingredients, type, rating));
        printSymbols();
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*New pizza added*" + ConsoleColor.RESET.getCode());
    }

    public static void delete_pizza() {
    }

    public static void main_promo() {
        while (true) {
            switch (start_managing_promo()) {
                case 1:
                    add_new_promo();
                    break;
                case 2:
                    delete_promo();
                    break;
                case 3:
                    return;
            }
        }
    }

    public static void add_new_promo() {
    }

    public static void delete_promo() {
    }

    public static int start_login() {
        while (true) {
            printSymbols();
            System.out.print("(1) ADMIN ||| (2) CHIEF ADMIN || " + ConsoleColor.RED.getCode() + "(3) Exit ---| " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 3) {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                continue;
            }
            return choice;
        }
    }

    public static int start_managing() {
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
            }
            return choice;
        }
    }

    public static int start_managing_admins() {
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
            }
            return choice;
        }
    }

    public static int start_managing_pizza() {
        while (true) {
            printSymbols();
            print_pizzas();
            printSymbols();
            System.out.print("(1) Add new pizza || (2) Delete pizza || " + ConsoleColor.RED.getCode() + "(3) Back ---| " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 3 /* rewrite */) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
            } else if (choice == 3) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            }
            return choice;
        }
    }

    public static int start_managing_promo() {
        while (true) {
            printSymbols();
            System.out.print("(1) Add new promo || (2) Delete promo || " + ConsoleColor.RED.getCode() + "(3) Back ---| " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 3 /* rewrite */) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
            } else if (choice == 3) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            }
            return choice;
        }
    }

    public static void printSymbols() {
        System.out.println(ConsoleColor.BLUE.getCode() + "*****************************************************************************************************************************************************************************" + ConsoleColor.RESET.getCode());
    }
}


