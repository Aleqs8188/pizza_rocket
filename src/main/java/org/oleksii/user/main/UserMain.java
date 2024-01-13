package org.oleksii.user.main;

import org.oleksii.admin.promotional_code_for_admin.Promo;
import org.oleksii.enums.ConsoleColor;
import org.oleksii.pizzas.PizzasList;
import org.oleksii.reviews.ReviewList;
import org.oleksii.user.client.Client;
import org.oleksii.user.info.Address;
import org.oleksii.user.info.PaymentInfo;
import org.oleksii.user.orders.OrdersOfAllTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.oleksii.user.client.ClientsList.clientArrayList;
import static org.oleksii.user.databaseAccessors.ClientDatabaseAccessor.*;
import static org.oleksii.user.databaseAccessors.PizzaDatabaseAccessorForUser.getPizzaFromBDByParameters;
import static org.oleksii.user.databaseAccessors.PromoDatabaseAccessorForUser.checker_deleter_promos;
import static org.oleksii.user.databaseAccessors.PromoDatabaseAccessorForUser.get_active_promo_from_db;
import static org.oleksii.user.databaseAccessors.ReviewDatabaseAccessorForUser.*;
import static org.oleksii.user.orders.CurrentOrder.*;
import static org.oleksii.user.orders.OrdersOfAllTime.printAllOrders;

public class UserMain {
    static Scanner scanner = new Scanner(System.in);
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static int choice;
    static Client clientLog;

    public static void clientMain() throws IOException {
        while (true) {
            switch (start_for_client_registration_login()) {
                case 1:
                    registration_new_client();
                case 2:
                    login_client();
                    break;
                case 3:
                    return;
            }
        }
    }

    public static void make_an_order_for_client(Promo promo) {
        printSymbols();
        if (totalSum() == 0) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Your shopping basket is empty... To make an order you need to order at least 1 pizza!" + ConsoleColor.RESET.getCode());
            return;
        }
        double distance = realizeAnOrder("50-001", clientLog.getAddress().getPostalCode());
        if (distance > 0) {
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "************************************************************************************You ordered******************************************************************************" + ConsoleColor.RESET.getCode());
            printOrders();
            printSymbols();
            double costDelivery = distance * 2;
            double sumOfOrderAndDelivery = costDelivery + totalSum();
            int discount = 0;
            double finalCost = 0;
            if (promo != null) {
                discount = promo.getDiscount();
                finalCost = sumOfOrderAndDelivery - (discount * sumOfOrderAndDelivery / 100);
            }
            display_order_summary(distance, costDelivery, sumOfOrderAndDelivery, discount, finalCost);
            addAnOrderToDB(clientLog);
            System.exit(0);
        }
    }

    public static void print_orders_of_all_time_for_client() {
        OrdersOfAllTime ordersOfAllTime = new OrdersOfAllTime(getOrdersFromDB(clientLog));
        printSymbols();
        if (ordersOfAllTime.clientOrders == null || ordersOfAllTime.clientOrders.length == 0) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "You haven't made any orders yet" + ConsoleColor.RESET.getCode());
            return;
        }
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "**********************************************************************Your orders for all time*******************************************************************************" + ConsoleColor.RESET.getCode());

        printAllOrders(getOrdersFromDB(clientLog));
    }

    public static void total_amount_of_order() {
        if (totalSum() == 0) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Your shopping basket is empty..." + ConsoleColor.RESET.getCode());
            return;
        }
        printSymbols();
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Total amount of your order: " + totalSum() + ConsoleColor.RESET.getCode());
    }

    public static void delete_pizza_from_order() throws IOException {
        while (true) {
            printSymbols();
            try {
                System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want back - type ---> '9'" + ConsoleColor.RESET.getCode());
                System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Enter pizza name you want to delete: " + ConsoleColor.RED.getCode());
                String pizzaToDelete = reader.readLine();
                if (pizzaToDelete.equals("9")) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                    return;
                }
                if (deleteObjectFromOrder(pizzaToDelete) == null) {
                    printSymbols();
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Enter correct name of Pizza..." + ConsoleColor.RESET.getCode());
                } else {
                    printSymbols();
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*This pizza has been removed from your order...." + ConsoleColor.RESET.getCode());
                    if (totalSum() == 0) {
                        System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Your shopping basket is empty..." + ConsoleColor.RESET.getCode());
                    }
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Enter correct id of Pizza..." + ConsoleColor.RESET.getCode());
            }
        }
    }

    public static int start_of_managing_with_orders() {
        if (totalSum() == 0) {
            printSymbols();
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Your shopping basket is empty..." + ConsoleColor.RESET.getCode());
            return 3;
        }
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*****************************************************************************Your shopping cart******************************************************************************" + ConsoleColor.RESET.getCode());
            printOrders();
            printSymbols();
            System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Choose what do you want to do: ");
            System.out.print("1) Delete pizza from order || 2) Count total sum from order || " + ConsoleColor.RED.getCode() + "3) Back --- " + ConsoleColor.RESET.getCode());
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

    public static String start_of_selecting_pizza() throws IOException {
        String pizzaName;
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Enter the pizza name you want to purchase: " + ConsoleColor.RESET.getCode());
            pizzaName = reader.readLine();
            if (pizzaName.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return null;
            }
            if (getPizzaFromBDByParameters(pizzaName) != null) {
                return pizzaName;
            } else {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter an existing pizza..." + ConsoleColor.RESET.getCode());
            }
        }
    }

    public static int start_of_interaction_with_the_shopping_cart() {
        while (true) {
            try {
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "***********************************************************************************MENU**************************************************************************************" + ConsoleColor.RESET.getCode());
                PizzasList pizzasList = new PizzasList();
                pizzasList.print_pizzas();
                printSymbols();
                System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Choose what are you want: ");
                System.out.print("1) Add to shopping cart || 2) Check my shopping cart || " + ConsoleColor.RED.getCode() + "3) Back --- " + ConsoleColor.RESET.getCode());
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

    public static int start_of_interaction_with_the_client() {
        int choice;

        printSymbols();
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() +
                "*Remember, that delivery is not free, for distances " +
                ConsoleColor.RED.getCode() + "over 2 kilometers, " +
                ConsoleColor.GREEN.getCode() + "the delivery cost is " +
                ConsoleColor.RED.getCode() + "2 PLN per kilometer!");

        while (true) {
            try {
                printSymbols();
                System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() +
                        "*Choose what you want to do...");
                System.out.print("1) Check the Menu and add items to the list || " +
                        "2) Check all my orders for all time || " +
                        "3) Make an order || " +
                        "4) View/write a review || " +
                        ConsoleColor.RED.getCode() + "5) Exit --- ");

                choice = scanner.nextInt();

                if (choice > 5) {
                    printSymbols();
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() +
                            "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                } else if (choice == 5) {
                    System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() +
                            "<---Exit" + ConsoleColor.RESET.getCode());
                    break;
                }

                return choice;
            } catch (InputMismatchException ignored) {
                printSymbols();
                scanner.nextLine();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() +
                        "*Enter correct number..." + ConsoleColor.RESET.getCode());
            }
        }
        return choice;
    }

    public static void registration_new_client() throws IOException {
        int listSize = clientArrayList.size();
        printSymbols();
        System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "If you want to Log in - type ---> '9'" + ConsoleColor.RESET.getCode());
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "-Please, enter your data to register: ");
        System.out.println("*Personal information: ");
        System.out.print("          -Name: ");
        String nameReg = reader.readLine();
        if (nameReg.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        System.out.print("          -Surname: ");
        String surnameReg = reader.readLine();
        if (surnameReg.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        String emailReg;
        String phoneReg;
        while (true) {
            System.out.print("          -Phone number: ");
            phoneReg = reader.readLine();
            if (phoneReg.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "          -Email: ");
            emailReg = reader.readLine();
            if (emailReg.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            Client clientWithSameParameters = searchClientInBD(emailReg, phoneReg);
            if (clientWithSameParameters.getId() != 0) {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Person with this 'phone number' or 'email' already registered..." + ConsoleColor.RESET.getCode());
            } else {
                break;
            }
        }
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Address information: ");
        System.out.print("          -Street: ");
        String streetReg = reader.readLine();
        if (streetReg.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        System.out.print("          -City: ");
        String cityReg = reader.readLine();
        if (cityReg.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        String postalCodeReg;
        while (true) {
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "          -Postal code of Wroclaw: " + ConsoleColor.RESET.getCode());
            postalCodeReg = reader.readLine();
            if (postalCodeReg.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            if (postalCodeReg.length() == 6) {
                realizeAnOrder("50-001", postalCodeReg);
                break;
            }
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Enter your real postal code!" + ConsoleColor.RESET.getCode());
        }
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Payment information:");
        System.out.print("          -Credit Card Number: " + ConsoleColor.RESET.getCode());
        String creditCardNumberReg = reader.readLine();
        if (creditCardNumberReg.equals("9")) {
            System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
            return;
        }
        Client newClient = new Client(listSize + 1, nameReg, surnameReg, phoneReg, emailReg,
                new Address(streetReg, cityReg, postalCodeReg), new PaymentInfo(creditCardNumberReg, new Address(streetReg, cityReg, postalCodeReg)));
        if (addClientToBD(newClient)) {
            printSymbols();
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Successful registration!" + ConsoleColor.RESET.getCode());
        } else {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Oops, something happened, try again..." + ConsoleColor.RESET.getCode());
        }
    }

    public static void login_client() throws IOException {
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.BOLD.getCode() + ConsoleColor.RED.getCode() + "If you want back - type ---> '9'" + ConsoleColor.RESET.getCode());
            System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Please, enter your data to Log in:");
            System.out.print("          -Email: ");
            String emailLog = reader.readLine();
            if (emailLog.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            System.out.print("          -Phone number: ");
            String phoneNumberLog = reader.readLine();
            if (phoneNumberLog.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            System.out.print("          -Credit card number: " + ConsoleColor.RESET.getCode());
            String creditCardNumberLog = reader.readLine();
            if (creditCardNumberLog.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
            clientLog = searchClientInBD(emailLog, phoneNumberLog, creditCardNumberLog);
            if (clientLog.getId() == 0) {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "****************This user does not exist****************");
                printSymbols();
            } else {
                printSymbols();
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Successful login" + ConsoleColor.RESET.getCode());
                printSymbols();
                break;
            }
        }
        assert clientLog != null;
        checker_deleter_promos();
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Hello '" + clientLog.getFirstName() + " " + clientLog.getLastName() + "', you are in PizzaRocket!");
        Promo promo = get_active_promo_from_db();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (promo != null) {
            System.out.println(ConsoleColor.GREEN.getCode() + "You have '" + ConsoleColor.RED.getCode() + promo.getDiscount() + ConsoleColor.GREEN.getCode() + "%' rebate for your order! The promo is valid until " + ConsoleColor.RED.getCode() + promo.getEnd_date().format(formatter) + ConsoleColor.RESET.getCode());
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + promo.getDescription());
        }
        while (true) {
            switch (start_of_interaction_with_the_client()) {
                case 1:
                    switch (start_of_interaction_with_the_shopping_cart()) {
                        case 1:
                            String test = start_of_selecting_pizza();
                            if (test == null) {
                                break;
                            } else {
                                order.add(getPizzaFromBDByParameters(test));
                                printSymbols();
                                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Pizza '" + test + "' has been successfully added to your order!" + ConsoleColor.RESET.getCode());
                            }
                            break;
                        case 2:
                            switch (start_of_managing_with_orders()) {
                                case 1:
                                    delete_pizza_from_order();
                                    break;
                                case 2:
                                    total_amount_of_order();
                                    break;
                                case 3:
                                    break;
                            }
                        case 3:
                            break;
                    }
                    break;
                case 2:
                    switch (start_of_managing_with_orders_from_all_time()) {
                        case 1:
                            clear_history_orders_from_all_time();
                            break;
                        case 2:
                            //!
                            break;
                        case 3:
                            //!
                            break;
                    }
                    break;
                case 3:
                    make_an_order_for_client(promo);
                    break;
                case 4:
                    switch (start_review_for_client()) {
                        case 1:
                            ReviewList reviewList;
                            switch (start_ordered_review_for_client()) {
                                case 1:
                                    reviewList = new ReviewList(get_reviews_from_db_ordered_by_old_date());
                                    reviewList.print_reviews();
                                    break;
                                case 2:
                                    reviewList = new ReviewList(get_reviews_from_db_ordered_by_new_date());
                                    reviewList.print_reviews();
                                    break;
                                case 3:
                                    reviewList = new ReviewList(get_reviews_from_db_ordered_by_best_rating());
                                    reviewList.print_reviews();
                                    break;
                                case 4:
                                    reviewList = new ReviewList(get_reviews_from_db_ordered_by_worst_rating());
                                    reviewList.print_reviews();
                                    break;
                            }
                            break;
                        case 2:
                            write_review_for_client();
                            break;
                        case 3:
                            return;
                    }
                    break;
                case 5:
                    return;
            }
        }
    }

    private static int start_ordered_review_for_client() {
        while (true) {
            try {
                printSymbols();
                System.out.print("1) Sort by new reviews || 2) Sorted by old reviews || 3) Sort by best reviews || 4) Sort by worst reviews || " + ConsoleColor.RED.getCode() + "5) Back --- " + ConsoleColor.RESET.getCode());
                choice = scanner.nextInt();
                if (choice > 5) {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                    continue;
                } else if (choice == 5) {
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

    private static void write_review_for_client() throws IOException {
        while (true) {
            printSymbols();
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "Enter rating of my Delivery Application (*-*****): " + ConsoleColor.RESET.getCode());
            String rating = reader.readLine();
            if (!rating.matches("^\\*.*")) {
                System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "Enter rating from '*' to '*****'" + ConsoleColor.RESET.getCode());
                continue;
            }
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "Enter review: " + ConsoleColor.RESET.getCode());
            String description = reader.readLine();
            if (add_review_to_db(rating, description, clientLog.getId())) {
                printSymbols();
                System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Your review has been left" + ConsoleColor.RESET.getCode());
                return;
            } else {
                printSymbols();
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "Something went wrong, try again" + ConsoleColor.RESET.getCode());
            }
        }
    }

    private static int start_review_for_client() {
        while (true) {
            try {
                printSymbols();
                System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Choose what are you want: ");
                System.out.print("1) View reviews || 2) Leave feedback || " + ConsoleColor.RED.getCode() + "3) Back --- " + ConsoleColor.RESET.getCode());
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


    private static void clear_history_orders_from_all_time() {
        if (clear_orders_in_db(clientLog)) {
            printSymbols();
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*Your order history has been cleared!" + ConsoleColor.RESET.getCode());
        }
    }

    // rework
    private static int start_of_managing_with_orders_from_all_time() {
        while (true) {
            try {
                print_orders_of_all_time_for_client();
                printSymbols();
                System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Choose what are you want: ");
                System.out.print("1) Clear history of orders || 2)  || " + ConsoleColor.RED.getCode() + "3) Back --- " + ConsoleColor.RESET.getCode());
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

    public static int start_for_client_registration_login() {
        while (true) {
            try {
                printSymbols();
                System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "Hello, you are in my 'Delivery Application (PizzaRocket)'\n" +
                        "Choose what do you want to do: 1) Registration / 2) Log in --- " + ConsoleColor.RESET.getCode());
                choice = scanner.nextInt();
                if (choice > 3) {
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Incorrect value, try again..." + ConsoleColor.RESET.getCode());
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

    public static void printSymbols() {
        System.out.println(ConsoleColor.BLUE.getCode() + "*****************************************************************************************************************************************************************************" + ConsoleColor.RESET.getCode());
    }
}