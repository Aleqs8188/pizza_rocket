package org.oleksii.admin.main;

import org.oleksii.admin.promotional_code_for_admin.Promo;
import org.oleksii.admin.promotional_code_for_admin.PromoList;
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
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.*;
import static org.oleksii.admin.databaseAccessors.AdminDatabaseAccessor.getAdminFromDB;
import static org.oleksii.admin.databaseAccessors.PizzaDatabaseAccessorForAdmin.*;
import static org.oleksii.admin.databaseAccessors.PromoDatabaseAccessorForAdmin.*;
import static org.oleksii.admin.super_users.default_administrator.AdminsList.print_details_about_admin;
import static org.oleksii.user.databaseAccessors.PromoDatabaseAccessorForUser.checker_deleter_promos;

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
        checker_deleter_promos();
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
                        reset_username();
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
                        printSymbols();
                        System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "If you do not remember your 'password', contact the main administrator..." + ConsoleColor.RESET.getCode());
                        break firstCaseLoop;
                    }
                }
                counter_2++;
            }
            while (true) {
                switch (start_managing_for_admin()) {
                    case 1:
                        main_pizza();
                        break;
                    case 2:
                        main_promo();
                        break;
                    case 3:
                        main_recovery_code(adminLog);
                        break;
                    case 4:
                        return;
                }
            }
        }
    }

    public static void main_recovery_code(Admin admin) throws IOException {
        switch (start_managing_recovery_code()) {
            case 1:
                check_recovery_code(admin);
                break;
            case 2:
                make_change_my_code(admin);
                break;
            case 3:
                break;
        }
    }

    private static void make_change_my_code(Admin admin) throws IOException {
        String secret_code = get_secret_code_from_db(admin);
        printSymbols();
        if (secret_code == null) {
            System.out.print(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "Enter a secret code that only you know: " + ConsoleColor.RESET.getCode());
            String secret_code_register = reader.readLine();
            if (searcher_the_same_secret_code(secret_code_register) == null) {
                changer_secret_code(admin, secret_code_register);
                printSymbols();
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "Your secret code has been created!" + ConsoleColor.RESET.getCode());
            } else {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Doesn't fit" + ConsoleColor.RESET.getCode());
            }
        } else {
            System.out.print(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "Enter your old secret code: " + ConsoleColor.RESET.getCode());
            String old_secret_code = reader.readLine();
            if (secret_code.equals(old_secret_code)) {
                System.out.print(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "Enter your new secret code: " + ConsoleColor.RESET.getCode());
                String new_secret_code = reader.readLine();
                if (searcher_the_same_secret_code(new_secret_code) == null) {
                    changer_secret_code(admin, new_secret_code);
                    save_changes_to_db(admin, "Secret code", old_secret_code, new_secret_code);
                    printSymbols();
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "Your secret code has been changed!" + ConsoleColor.RESET.getCode());
                }
            } else {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + "Incorrect!" + ConsoleColor.RESET.getCode());
            }
        }
    }

    public static void check_recovery_code(Admin admin) throws IOException {
        String secret_code = get_secret_code_from_db(admin);
        printSymbols();
        if (secret_code == null) {
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.BLACK.getCode() + "You need make your secret code!" + ConsoleColor.RESET.getCode());
            make_change_my_code(admin);
            return;
        }
        System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.BLACK.getCode() + secret_code + ConsoleColor.RESET.getCode());
    }

    public static int start_managing_recovery_code() {
        while (true) {
            try {
                printSymbols();
                System.out.print("(1) Check my code || (2) Make/change my code || " + ConsoleColor.RED.getCode() + "(3) Exit ---| " + ConsoleColor.RESET.getCode());
                choice = scanner.nextInt();
                if (choice > 3) {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                    continue;
                } else if (choice == 3) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Exit" + ConsoleColor.RESET.getCode());
                }
                return choice;
            } catch (InputMismatchException ignored) {
                printSymbols();
                scanner.nextLine();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() +
                        "*Enter correct number..." + ConsoleColor.RESET.getCode());
            }
        }
    }

    private static void reset_username() throws IOException {
        printSymbols();
        System.out.println(ConsoleColor.BOLD.getCode() + "If you don`t remember your username, you need enter your secret code, to reset.");
        printSymbols();
        System.out.print(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "Enter secret code: " + ConsoleColor.RESET.getCode());
        String secretCode = reader.readLine();
        Admin admin = get_admin_by_secret_code(secretCode);
        if (admin != null) {
            String old_username = admin.getUsername();
            while (true) {
                System.out.print(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "Enter new Username: " + ConsoleColor.RESET.getCode());
                String newUsername = reader.readLine();
                if (getAdminFromDB(newUsername) == null) {
                    if (change_username_for_admin_in_db(admin, newUsername)) {
                        printSymbols();
                        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Your username successfully changed!" + ConsoleColor.RESET.getCode());
                        if (save_changes_to_db(admin, "Username", old_username, newUsername)) {
                            return;
                        }
                    }
                } else {
                    printSymbols();
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "This username is already taken!" + ConsoleColor.RESET.getCode());
                    printSymbols();
                }
            }
        } else {
            printSymbols();
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect secret code..." + ConsoleColor.RESET.getCode());
        }
    }

    public static int start_managing_for_admin() {
        while (true) {
            printSymbols();
            System.out.print("(1) Managing pizza || (2) Managing promo || (3) Recovery code || " + ConsoleColor.RED.getCode() + "(4) Exit ---| " + ConsoleColor.RESET.getCode());
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
                switch (start_managing_for_super_admin()) {
                    case 1:
                        main_administrator();
                        break;
                    case 2:
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
                    print_details_about_admin();
                    break;
                case 4:
                    return;
            }
        }
    }

    public static void add_new_administrator() throws IOException {
        printSymbols();
        int idNewAdmin = getAllIdFromDB();
        String userNameNewAdmin;
        System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
        System.out.print(ConsoleColor.BOLD.getCode() + "Name: " + ConsoleColor.RESET.getCode());
        String nameNewAdmin = reader.readLine();
        if (nameNewAdmin.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        System.out.print(ConsoleColor.BOLD.getCode() + "Surname: " + ConsoleColor.RESET.getCode());
        String surNameNewAdmin = reader.readLine();
        if (surNameNewAdmin.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        while (true) {
            System.out.print(ConsoleColor.BOLD.getCode() + "Username: " + ConsoleColor.RESET.getCode());
            userNameNewAdmin = reader.readLine();
            if (getAdminFromDB(userNameNewAdmin) != null) {
                System.out.println(ConsoleColor.RED.getCode() + "*An administrator with the username '" + userNameNewAdmin + "' already exists, select a new username");
                printSymbols();
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
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter username to delete: " + ConsoleColor.RESET.getCode());
            String usernameToDelete = reader.readLine();
            if (usernameToDelete.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (getAdminFromDB(usernameToDelete) != null) {
                if (deleteAdminFromDB(usernameToDelete)) {
                    printSymbols();
                    System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Admin with username '" + usernameToDelete + "'" + " successfully deleted" + ConsoleColor.RESET.getCode());
                    break;
                }
            } else {
                if (counter > 5) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                    break;
                } else {
                    printSymbols();
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
            if (name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
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
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter name of pizza to delete: " + ConsoleColor.RESET.getCode());
            String pizzaNameToDelete = reader.readLine();
            if (pizzaNameToDelete.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
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
        System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
        String name;
        while (true) {
            name = checking_values_for_adding(16, "('LOVE23SPECIAL')", "Name: ", "Wrong name...");
            if (name.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (get_promo_from_db_by_name(name)) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*An promo with name '" + name + "' already exists");
                printSymbols();
                System.out.println("*Choose new name for promo" + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        int discount;
        while (true) {
            try {
                discount = Integer.parseInt(checking_values_for_adding(3, "(5%-100%)", "Discount: ", "Wrong discount..."));
                if (discount == 9) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                    return;
                }
                if (discount < 5 || discount > 100) {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "You need to enter the discount percentage from 5% to 100%, not " + discount + "%" + ConsoleColor.RESET.getCode());
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*The entered value contains characters other than numbers" + ConsoleColor.RESET.getCode());
            }
        }
        String description;
        while (true) {
            description = checking_values_for_adding(170, "('LOVE23SPECIAL' invites you to unlock exclusive Valentine's Day deals, adding a touch of romance to your special moments. Enjoy unique offers and celebrate with us!)", "Description: ", "Wrong description...");
            if (description.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (description.length() > 170 || description.length() < 10) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Allowable description length is from 10 to 170 characters..." + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        String end_promoStr;
        LocalDateTime end_promo;
        while (true) {
            end_promoStr = checking_values_for_adding(16, "(yyyy-MM-dd HH-mm)", "End promo day: ", "Wrong date...");
            if (end_promoStr.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            try {
                end_promo = LocalDateTime.parse(end_promoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                break;
            } catch (DateTimeParseException e) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect date entry, you should enter '2023-12-31 23:59' using this example" + ConsoleColor.RESET.getCode());
            }
        }
        boolean active = Boolean.parseBoolean(checking_values_for_adding(5, "(true-false)", "Active?: ", "Wrong active..."));
        if (String.valueOf(active).equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        add_promo_to_db(new Promo(name, discount, description, LocalDateTime.now(), end_promo, active));
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*New promo added*" + ConsoleColor.RESET.getCode());
    }

    public static void delete_promo() throws IOException {
        int counter = 1;
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want to back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter name of promo to delete: " + ConsoleColor.RESET.getCode());
            String promoNameToDelete = reader.readLine();
            if (promoNameToDelete.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            printSymbols();
            if (get_promo_from_db_by_name(promoNameToDelete)) {
                if (delete_promo_from_db(promoNameToDelete)) {
                    System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "Promo with name '" + promoNameToDelete + "'" + " successfully deleted" + ConsoleColor.RESET.getCode());
                    break;
                }
            } else {
                if (counter >= 5) {
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
        }
    }

    public static int start_login() {
        while (true) {
            try {
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
            } catch (InputMismatchException ignored) {
                printSymbols();
                scanner.nextLine();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() +
                        "*Enter correct number..." + ConsoleColor.RESET.getCode());
            }
        }
    }

    public static int start_managing_for_super_admin() {
        while (true) {
            try {
                printSymbols();
                System.out.print("(1) Managing Administrators || " + ConsoleColor.RED.getCode() + "(2) Exit ---| " + ConsoleColor.RESET.getCode());
                choice = scanner.nextInt();
                if (choice > 2) {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                    continue;
                } else if (choice == 2) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Exit" + ConsoleColor.RESET.getCode());
                }
                return choice;
            } catch (InputMismatchException ignored) {
                printSymbols();
                scanner.nextLine();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() +
                        "*Enter correct number..." + ConsoleColor.RESET.getCode());
            }
        }
    }

    public static int start_managing_admins() {
        AdminsList admins = new AdminsList();
        printSymbols();
        admins.printAllAdmins();
        printSymbols();
        System.out.print("(1) Add new administrator || (2) Delete administrator || (3) Get details about administrator || " + ConsoleColor.RED.getCode() + "(4) Back ---| " + ConsoleColor.RESET.getCode());
        choice = scanner.nextInt();
        if (choice > 4) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
        } else if (choice == 4) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
        }
        return choice;
    }

    public static int start_managing_pizza() {
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

    public static int start_managing_promo() {
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

    public static int start_managing_promo_configuration() {
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
            if (discount.matches("\\d+")) {
                if (changer_promo_discount_in_db(discount, promo_name)) {
                    printSymbols();
                    System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Changes made successfully..." + ConsoleColor.RESET.getCode());
                    return;
                } else {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Something wrong!Try again..." + ConsoleColor.RESET.getCode());
                }
            } else {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*The entered value contains characters other than numbers" + ConsoleColor.RESET.getCode());
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
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD + "Something wrong!Try again..." + ConsoleColor.RESET.getCode());
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
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD + "Something wrong!Try again..." + ConsoleColor.RESET.getCode());
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
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD + "Something wrong!Try again..." + ConsoleColor.RESET.getCode());
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
                break;
            }
        }
        return returnStr;
    }

    public static void printSymbols() {
        System.out.println(ConsoleColor.BLUE.getCode() + "******************************************************************************************************************************************************************************" + ConsoleColor.RESET.getCode());
    }
}