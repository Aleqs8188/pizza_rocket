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
import static org.oleksii.user.databaseAccessors.PizzaDatabaseAccessorForUsers.addAnOrderToDB;
import static org.oleksii.user.databaseAccessors.PizzaDatabaseAccessorForUsers.getPizzaFromBDByParameters;
import static org.oleksii.user.orders.CurrentOrder.*;

//2) make print OrdersOfAllTime
//4) checker on exception JSonArray to rewrite new postal code
//5) make adding and delete pizza to order by name
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
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "************************************************************************************You ordered******************************************************************************" + ConsoleColor.RESET.getCode());
        printOrders();
        printSymbols();
        double distance = realizeAnOrder("50-001", clientLog.getAddress().getPostalCode());
        double costDelivery = distance * 2;
        double sumOfOrderAndDelivery = costDelivery + totalSum();
        display_order_summary(distance, costDelivery, sumOfOrderAndDelivery);
        addAnOrderToDB(clientLog);
        System.exit(0);
    }

    public static void print_orders_of_all_time_for_client() {
        OrdersOfAllTime clientOrders = new OrdersOfAllTime(getOrdersFromDB(clientLog));
        printSymbols();
        if (clientOrders.getOrders() == null) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "You haven't made any orders yet" + ConsoleColor.RESET.getCode());
            return;
        }
        System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "**********************************************************************Your orders for all time*******************************************************************************" + ConsoleColor.RESET.getCode());
        clientOrders.printAllOrders();
    }

    public static void total_amount_of_order() {
        if (totalSum() == 0) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Your shopping basket is empty..." + ConsoleColor.RESET.getCode());
            return;
        }
        printSymbols();
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Total amount of your order: " + totalSum() + ConsoleColor.RESET.getCode());
    }

    public static void delete_pizza_from_order() {
        while (true) {
            printSymbols();
            try {
                System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Enter id pizza you want to delete: " + ConsoleColor.RED.getCode());
                int idPizzaToDelete = scanner.nextInt();
                if (deleteObjectFromOrder(idPizzaToDelete) == null) {
                    printSymbols();
                    System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Enter correct id of Pizza..." + ConsoleColor.RESET.getCode());
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
            System.out.print("1) Delete something from order || 2) Count total sum from order || " + ConsoleColor.RED.getCode() + "3) Back --- " + ConsoleColor.RESET.getCode());
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

    public static int start_of_selecting_pizza() {
        int idPizza;
        while (true) {
            printSymbols();
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Select the pizza you want to purchase: " + ConsoleColor.RESET.getCode());
            idPizza = scanner.nextInt();
            if (idPizza > 30) {
                System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "*Choose the correct pizza ID" + ConsoleColor.RESET.getCode());
            } else {
                return idPizza;
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
        printSymbols();
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Remember, that delivery is not free, for distances " + ConsoleColor.RED.getCode() + "over 2 kilometers, " +
                ConsoleColor.CYAN.getCode() + "the delivery cost is " + ConsoleColor.RED.getCode() + "2 PLN per kilometer!");
        while (true) {
            printSymbols();
            System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Choose what you want to do...");
            System.out.print("1) Check the Menu and add items to the list || " +
                    "2) Check all my orders for all time || " +
                    "3) Make an order || " +
                    ConsoleColor.RED.getCode() + "4) Exit --- ");
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

    public static void registration_new_client() throws IOException {
        int listSize = clientArrayList.size();
        printSymbols();
        System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "-Please, enter your data to register: ");
        System.out.println("*Personal information: ");
        System.out.print("          -Name: ");
        String nameReg = reader.readLine();
        System.out.print("          -Surname: ");
        String surnameReg = reader.readLine();
        System.out.print("          -Phone number: ");
        String phoneReg = reader.readLine();
        String emailReg;
        while (true) {
            System.out.print(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "          -Email: ");
            emailReg = reader.readLine();
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
        System.out.print("          -City: ");
        String cityReg = reader.readLine();
        System.out.print("          -Postal code: ");
        String postalCodeReg = reader.readLine();
        System.out.println("*Payment information:");
        System.out.print("          -Credit Card Number: " + ConsoleColor.RESET.getCode());
        String creditCardNumberReg = reader.readLine();
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
            System.out.println(ConsoleColor.CYAN.getCode() + ConsoleColor.BOLD.getCode() + "*Please, enter your data to Log in:");
            System.out.print("          -Email: ");
            String emailLog = reader.readLine();
            System.out.print("          -Phone number: ");
            String phoneNumberLog = reader.readLine();
            System.out.print("          -Credit card number: " + ConsoleColor.RESET.getCode());
            String creditCardNumberLog = reader.readLine();
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
                            int idPizza = start_of_selecting_pizza();
                            switch (idPizza) {
                                case 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30:
                                    if (getPizzaFromBDByParameters(idPizza) != null)
                                        order.add(getPizzaFromBDByParameters(idPizza));
                                    printSymbols();
                                    System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "*This pizza has been successfully added to your order!" + ConsoleColor.RESET.getCode());
                                    break;
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