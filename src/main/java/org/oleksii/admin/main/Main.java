package org.oleksii.admin.main;

import org.oleksii.admin.promotional_code.Promo;
import org.oleksii.admin.promotional_code.PromoList;
import org.oleksii.admin.super_users.chief_administrator.SuperAdmin;
import org.oleksii.admin.super_users.default_administrator.Admin;
import org.mindrot.jbcrypt.BCrypt;
import org.oleksii.admin.super_users.default_administrator.AdminsList;
import org.oleksii.enums.ConsoleColor;
import org.oleksii.pizzas.Pizza;
import org.oleksii.pizzas.PizzasList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.*;
import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.getAdminFromDB;
import static org.oleksii.admin.databaseAccessors.PizzaDatabaseAccessorForAdmins.*;
import static org.oleksii.admin.databaseAccessors.PromoDatabaseAccessor.*;

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
                        main_administrator();
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

    public static void main_administrator() throws IOException {
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
                System.out.println(ConsoleColor.RED.getCode() + "*An administrator with the username '" + userNameNewAdmin + "' already exists, select a new username");
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
        int counter = 1;
        while (true) {
            printSymbols();
            System.out.print(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter username to delete: " + ConsoleColor.RESET.getCode());
            String usernameToDelete = reader.readLine();
            if (getAdminFromDB(usernameToDelete) != null) {
                if (deleteAdminFromDB(usernameToDelete)) {
                    printSymbols();
                    System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "Admin with username '" + usernameToDelete + "'" + " successfully deleted" + ConsoleColor.RESET.getCode());
                    break;
                }
            } else {
                if (counter > 5) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                    break;
                } else {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter an existing admin..." + ConsoleColor.RESET.getCode());
                    counter++;
                }
            }
        }
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

    public static void add_new_pizza() throws IOException {
        printSymbols();
        String name;
        while (true) {
            name = checking_values_for_adding(20, "(Pepperoni)", "Name: ", "Wrong name...");
            if (get_pizza_from_db_by_name(name) != null) {
                System.out.println(ConsoleColor.RED.getCode() + "*An Pizza with name '" + name + "' already exists");
                System.out.println("*Choose new name for pizza" + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        add_pizza_to_db(new Pizza(name,
                checking_values_for_adding(44, "(Authentic Italian buffalo mozzarella)", "Description: ", "Wrong description..."),
                Double.parseDouble(checking_values_for_adding(5, "(1-999.99 PLN)", "Price: ", "Wrong price...")),
                checking_values_for_adding(7, "(Small, Medium, Large)", "Size: ", "Wrong size..."),
                checking_values_for_adding(52, "(Ham, artichokes, olives, mushrooms)", "Ingredients: ", "Wrong ingredients..."),
                checking_values_for_adding(12, "(Classic, Meat Lovers, Vegetarian, Specialty)", "Type: ", "Wrong type of pizza..."),
                checking_values_for_adding(5, "(*-*****)", "Rating: ", "Invalid number of characters...")));
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*New pizza added*" + ConsoleColor.RESET.getCode());
    }

    public static void delete_pizza() throws IOException {
        int counter = 1;
        while (true) {
            printSymbols();
            System.out.print(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter name of pizza to delete: " + ConsoleColor.RESET.getCode());
            String pizzaNameToDelete = reader.readLine();
            printSymbols();
            if (get_pizza_from_db_by_name(pizzaNameToDelete) != null) {
                if (delete_pizza_from_db(pizzaNameToDelete)) {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Pizza with name '" + pizzaNameToDelete + "'" + " successfully deleted" + ConsoleColor.RESET.getCode());
                    break;
                }
            } else {
                if (counter > 5) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                    break;
                } else {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter an existing pizza..." + ConsoleColor.RESET.getCode());
                    counter++;
                }
            }
        }
    }

    public static void main_promo() throws IOException {
        while (true) {
            switch (start_managing_promo()) {
                case 1:
                    add_new_promo();
                    break;
                case 2:
                    delete_promo();
                    break;
                case 3:
                    change_promo_status();
                    break;
                case 4:
                    return;
            }
        }
    }

    public static void add_new_promo() throws IOException {
        printSymbols();
        System.out.println("If you want back - type ---> '9'");
        String name;
        while (true) {
            name = checking_values_for_adding(16, "('LOVE23SPECIAL')", "Name: ", "Wrong name...");
            if (get_promo_from_db_by_name(name)) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*An promo with name '" + name + "' already exists");
                System.out.println("*Choose new name for promo" + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        if (name.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        int discount;
        while (true) {
            discount = Integer.parseInt(checking_values_for_adding(3, "(5%-100%)", "Discount: ", "Wrong discount..."));
            if (discount < 5 || discount > 100) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "You need to enter the discount percentage from 5% to 100%, not " + discount + "%" + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        if (discount == 9) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        String description;
        while (true) {
            description = checking_values_for_adding(170, "('LOVE23SPECIAL' invites you to unlock exclusive Valentine's Day deals, adding a touch of romance to your special moments. Enjoy unique offers and celebrate with us!)", "Description: ", "Wrong description...");
            if (description.length() > 170 || description.length() < 10) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Allowable description length is from 10 to 170 characters..." + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        if (description.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        String end_promoStr;
        LocalDateTime end_promo;
        while (true) {
            end_promoStr = checking_values_for_adding(16, "(yyyy-MM-dd HH-mm)", "End promo day: ", "Wrong date...");
            try {
                end_promo = LocalDateTime.parse(end_promoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                break;
            } catch (DateTimeParseException e) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect date entry, you should enter '2023-12-31 23:59' using this example" + ConsoleColor.RESET.getCode());
            }
        }
        if (end_promoStr.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        boolean active = Boolean.parseBoolean(checking_values_for_adding(5, "(true-false)", "Active?: ", "Wrong active..."));
        add_promo_to_db(new Promo(name, discount, description, LocalDateTime.now(), end_promo, active));
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*New promo added*" + ConsoleColor.RESET.getCode());
    }

    public static void delete_promo() throws IOException {
        int counter = 1;
        while (true) {
            printSymbols();
            System.out.print(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter name of promo to delete: " + ConsoleColor.RESET.getCode());
            String promoNameToDelete = reader.readLine();
            printSymbols();
            if (get_promo_from_db_by_name(promoNameToDelete)) {
                if (delete_promo_from_db(promoNameToDelete)) {
                    System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "Promo with name '" + promoNameToDelete + "'" + " successfully deleted" + ConsoleColor.RESET.getCode());
                    break;
                }
            } else {
                if (counter > 5) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                    break;
                } else {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter an existing promo..." + ConsoleColor.RESET.getCode());
                    counter++;
                }
            }
        }
    }

    public static void change_promo_status() throws IOException {
        printSymbols();
        switch (start_managing_promo_configuration()) {
            case 1:
                change_promo_name();
                break;
            case 2:
                change_promo_discount();
                break;
            case 3:
                change_promo_description();
                break;
            case 4:
                change_promo_end_date();
                break;
            case 5:
                change_promo_active();
                break;
            case 6:
                return;
        }
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
            } else if (choice == 3) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Exit" + ConsoleColor.RESET.getCode());
                printSymbols();
            }
            return choice;
        }
    }

    public static int start_managing() {
        while (true) {
            printSymbols();
            System.out.print("(1) Managing Administrators || (2) Managing Pizzas || (3) Managing Promos || " + ConsoleColor.RED.getCode() + "(4) Exit ---| " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 4) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                continue;
            } else if (choice == 4) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Exit" + ConsoleColor.RESET.getCode());
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
            if (choice > 3) {
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
            PizzasList pizzasList = new PizzasList();
            pizzasList.print_pizzas();
            printSymbols();
            System.out.print("(1) Add new pizza || (2) Delete pizza || " + ConsoleColor.RED.getCode() + "(3) Back ---| " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 3) {
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
            PromoList promoList = new PromoList();
            promoList.print_all_promo();
            printSymbols();
            System.out.print("(1) Add new promo || (2) Delete promo || (3) Change promo || " + ConsoleColor.RED.getCode() + "(4) Back ---| " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 4) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
            } else if (choice == 4) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            }
            return choice;
        }
    }

    public static int start_managing_promo_configuration() {
        while (true) {
            PromoList promoList = new PromoList();
            promoList.print_all_promo();
            printSymbols();
            System.out.print("(1) Change Name || (2) Change discount || (3) Change description || (4) Change end date || (5) Change status || " + ConsoleColor.RED.getCode() + "(6) Back ---| " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 6) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
            } else if (choice == 6) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            }
            return choice;
        }
    }

    public static void change_promo_name() throws IOException {
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print("Enter the name of the promotion you want to change: ");
            String old_name = reader.readLine();
            if (old_name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (!get_promo_from_db_by_name(old_name)) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There is no such promo with this name..." + ConsoleColor.RESET.getCode());
                continue;
            }
            System.out.print("Enter new name you want to change on: ");
            String new_name = reader.readLine();
            if (new_name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (!get_promo_from_db_by_name(new_name)) {
                changer_promo_name_in_db(new_name, old_name);
                printSymbols();
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Changes made successfully..." + ConsoleColor.RESET.getCode());
                return;
            } else {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "The promo with the same name already exist..." + ConsoleColor.RESET.getCode());
            }
        }
    }

    public static void change_promo_discount() throws IOException {
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print("Enter the name of the promotion you want to change: ");
            String promo_name = reader.readLine();
            if (promo_name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (!get_promo_from_db_by_name(promo_name)) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There is no such promo with this name..." + ConsoleColor.RESET.getCode());
                continue;
            }
            System.out.print("Enter new discount for this promo: ");
            String discount = reader.readLine();
            if (discount.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (changer_promo_discount_in_db(discount, promo_name)) {
                printSymbols();
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Changes made successfully..." + ConsoleColor.RESET.getCode());
                return;
            } else {
                System.out.println("Something wrong.Try again...");
            }
        }
    }

    public static void change_promo_description() throws IOException {
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print("Enter the name of the promotion you want to change: ");
            String promo_name = reader.readLine();
            if (promo_name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (!get_promo_from_db_by_name(promo_name)) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There is no such promo with this name..." + ConsoleColor.RESET.getCode());
                continue;
            }
            System.out.print("Enter new description for this promo: ");
            String description = reader.readLine();
            if (description.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (changer_promo_description_in_db(description, promo_name)) {
                printSymbols();
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Changes made successfully..." + ConsoleColor.RESET.getCode());
                return;
            } else {
                System.out.println("Something wrong.Try again...");
            }
        }
    }

    public static void change_promo_end_date() throws IOException {
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print("Enter the name of the promotion you want to change: ");
            String promo_name = reader.readLine();
            if (promo_name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (!get_promo_from_db_by_name(promo_name)) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There is no such promo with this name..." + ConsoleColor.RESET.getCode());
                continue;
            }
            System.out.print("Enter new end date for this promo: ");
            String end_date = reader.readLine();
            if (end_date.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (changer_promo_end_date_in_db(end_date, promo_name)) {
                printSymbols();
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Changes made successfully..." + ConsoleColor.RESET.getCode());
                return;
            } else {
                System.out.println("Something wrong.Try again...");
            }
        }
    }

    public static void change_promo_active() throws IOException {
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print("Enter the name of the promotion you want to change: ");
            String promo_name = reader.readLine();
            if (promo_name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (!get_promo_from_db_by_name(promo_name)) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "There is no such promo with this name..." + ConsoleColor.RESET.getCode());
                continue;
            }
            System.out.print("Enter new active for this promo: ");
            String active = reader.readLine();
            if (active.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (changer_promo_active_in_db(active, promo_name)) {
                printSymbols();
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Changes made successfully..." + ConsoleColor.RESET.getCode());
                return;
            } else {
                System.out.println("Something wrong.Try again...");
            }
        }
    }

    public static String checking_values_for_adding(int max, String example, String column, String wrong) throws IOException {
        String returnStr;
        while (true) {
            System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "For example: <" + example + ">, maximum '" + max + "' symbols" + ConsoleColor.RESET.getCode());
            System.out.print(ConsoleColor.BOLD.getCode() + column + ConsoleColor.RESET.getCode());
            returnStr = reader.readLine();
            if (returnStr.length() > max) {
                System.out.println(ConsoleColor.RED.getCode() + wrong + ConsoleColor.RESET.getCode());
                printSymbols();
            } else {
                printSymbols();
                break;
            }
        }
        return returnStr;
    }

    public static void printSymbols() {
        System.out.println(ConsoleColor.BLUE.getCode() + "*****************************************************************************************************************************************************************************" + ConsoleColor.RESET.getCode());
    }
}