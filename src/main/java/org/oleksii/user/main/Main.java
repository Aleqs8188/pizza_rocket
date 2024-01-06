package org.oleksii.user.main;

import org.oleksii.enums.ConsoleColor;
import org.oleksii.pizzas.PizzasList;
import org.oleksii.user.client.Client;
import org.oleksii.user.info.Address;
import org.oleksii.user.info.PaymentInfo;
import org.oleksii.user.orders.OrdersOfAllTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.oleksii.user.client.ClientsList.clientArrayList;
import static org.oleksii.user.databaseAccessors.ClientDatabaseAccessor.*;
import static org.oleksii.user.databaseAccessors.PizzaDatabaseAccessorForUsers.getPizzaFromBDByParameters;
import static org.oleksii.user.orders.CurrentOrder.*;
import static org.oleksii.user.orders.OrdersOfAllTime.printAllOrders;

//2) make print promo and communicate with him

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static int choice;
    static Client clientLog;

    public static void main(String[] args) throws IOException {
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

    public static void make_an_order_for_client() {
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
            display_order_summary(distance, costDelivery, sumOfOrderAndDelivery);
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
        }
    }

    public static int start_of_interaction_with_the_client() {
        int choice;

        printSymbols();
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() +
                "*Remember, that delivery is not free, for distances " +
                ConsoleColor.RED.getCode() + "over 2 kilometers, " +
                ConsoleColor.CYAN.getCode() + "the delivery cost is " +
                ConsoleColor.RED.getCode() + "2 PLN per kilometer!");

        while (true) {
            try {
                printSymbols();
                System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() +
                        "*Choose what you want to do...");
                System.out.print("1) Check the Menu and add items to the list || " +
                        "2) Check all my orders for all time || " +
                        "3) Make an order || " +
                        ConsoleColor.RED.getCode() + "4) Exit --- ");

                choice = scanner.nextInt();

                if (choice > 4) {
                    printSymbols();
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() +
                            "Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                } else if (choice == 4) {
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
        do {
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "          -Postal code of Wroclaw: " + ConsoleColor.RESET.getCode());
            postalCodeReg = reader.readLine();
            if (postalCodeReg.equals("9")) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Back" + ConsoleColor.RESET.getCode());
                return;
            }
        } while (!(realizeAnOrder("50-001", postalCodeReg) > 0));
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
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Hello '" + clientLog.getFirstName() + " " + clientLog.getLastName() + "', you are in PizzaRocket!");
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
                    print_orders_of_all_time_for_client();
                    //make button to order again
                    break;
                case 3:
                    make_an_order_for_client();
                    break;
                case 4:
                    return;
            }
        }
    }

    public static int start_for_client_registration_login() {
        while (true) {
            printSymbols();
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "Hello, you are in my 'Delivery Application (PizzaRocket)'\n" +
                    "Choose what do you want to do: 1) Registration / 2) Log in --- " + ConsoleColor.RESET.getCode());
            choice = scanner.nextInt();
            if (choice > 3) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Incorrect value, try again..." + ConsoleColor.RESET.getCode());
                printSymbols();
                continue;
            } else if (choice == 3) {
                System.out.println(ConsoleColor.BLACK.getCode() + ConsoleColor.BOLD.getCode() + "<---Exit" + ConsoleColor.RESET.getCode());
                printSymbols();
            }
            return choice;
        }
    }

    public static void printSymbols() {
        System.out.println(ConsoleColor.BLUE.getCode() + "*****************************************************************************************************************************************************************************" + ConsoleColor.RESET.getCode());
    }
}