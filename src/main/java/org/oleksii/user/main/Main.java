package org.oleksii.user.main;

import org.oleksii.user.databaseAccessors.ClientDatabaseAccessor;
import org.oleksii.user.client.Client;
import org.oleksii.user.info.Address;
import org.oleksii.user.info.PaymentInfo;
import org.oleksii.user.orders.OrdersOfAllTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Scanner;

import static org.oleksii.user.client.ClientsList.clientArrayList;
import static org.oleksii.user.databaseAccessors.ClientDatabaseAccessor.getOrdersFromDB;
import static org.oleksii.user.databaseAccessors.PizzaDatabaseAccessor.addAnOrderToDB;
import static org.oleksii.user.databaseAccessors.PizzaDatabaseAccessor.getPizzaFromBDByParameters;
import static org.oleksii.user.orders.CurrentOrder.*;
import static org.oleksii.pizzas.PizzasList.printPizzas;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            int choice;
            Client clientLog = null;
            System.out.print("Hello, you are in my 'Delivery Application (PizzaRocket)'\n" +
                    "Choose what do you want to do: 1) Registration / 2) Log in --- ");
            choice = scanner.nextInt();
            if (choice > 2) {
                System.err.println("Incorrect value, try again...");
                return;
            }
            switch (choice) {
                case 1:
                    int listSize = clientArrayList.size();
                    printSymbols();
                    System.out.println("-Please, enter your data to register: ");
                    System.out.println("*Personal information: ");
                    System.out.print("          -Name: ");
                    String nameReg = reader.readLine();
                    System.out.print("          -Surname: ");
                    String surnameReg = reader.readLine();
                    System.out.print("          -Phone number: ");
                    String phoneReg = reader.readLine();
                    System.out.print("          -Email: ");
                    String emailReg = reader.readLine();
                    Client clientWithSameParameters = ClientDatabaseAccessor.searchClientInBD(emailReg, phoneReg);
                    if (clientWithSameParameters.getId() != 0) {
                        System.err.println("Person with this 'phone number' or 'email' already registered...");
                        System.err.println("                     Try again...");
                        continue;
                    }
                    System.out.println("*Address information: ");
                    System.out.print("          -Street: ");
                    String streetReg = reader.readLine();
                    System.out.print("          -City: ");
                    String cityReg = reader.readLine();
                    System.out.print("          -Postal code: ");
                    String postalCodeReg = reader.readLine();
                    System.out.println("*Payment information:");
                    System.out.print("          -Credit Card Number: ");
                    String creditCardNumberReg = reader.readLine();
                    Client newClient = new Client(listSize + 1, nameReg, surnameReg, phoneReg, emailReg,
                            new Address(streetReg, cityReg, postalCodeReg), new PaymentInfo(creditCardNumberReg, new Address(streetReg, cityReg, postalCodeReg)));
                    if (ClientDatabaseAccessor.addClientToBD(newClient)) {
                        printSymbols();
                        System.out.println("-Successful registration!");
                    } else {
                        System.err.println("Oops, something happened, try again...");
                        continue;
                    }
                case 2:
                    printSymbols();
                    System.out.println("*Please, enter your data to Log in:");
                    System.out.print("          -Email: ");
                    String emailLog = reader.readLine();
                    System.out.print("          -Phone number: ");
                    String phoneNumberLog = reader.readLine();
                    System.out.print("          -Credit card number: ");
                    String creditCardNumberLog = reader.readLine();
                    clientLog = ClientDatabaseAccessor.searchClientInBD(emailLog, phoneNumberLog, creditCardNumberLog);
                    if (clientLog.getId() == 0) {
                        System.out.println("****************Try again****************");
                        continue;
                    }
                    System.out.println("**Successful login");
            }
            printSymbols();
            assert clientLog != null;
            System.out.println("*Hello '" + clientLog.getFirstName() + " " + clientLog.getLastName() + "', you are in PizzaRocket!");
            while (true) {
                printSymbols();
                System.out.println("*Remember, that delivery is not free, for distances over 2 kilometers, " +
                        "the delivery cost is 2 PLN per kilometer!!!");
                printSymbols();
                System.out.println("**Choose what do you want...");
                System.out.println("1) Check Menu and add to list || " +
                        "2) Check all my orders for all time  || " +
                        "3) Make an order  || " +
                        "4) Exit");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("***********************************************MENU***********************************************");
                        printPizzas();
                        printSymbols();
                        System.out.println("*Choose what are you want: ");
                        System.out.println("1) Add to shopping cart || 2) Check my shopping cart || 3) Back");
                        choice = scanner.nextInt();
                        switch (choice) {
                            case 1:
                                System.out.print("*Choose pizza you want to buy: ");
                                int idPizza = scanner.nextInt();
                                if (idPizza > 20) {
                                    System.out.println("*Choose the correct pizza ID");
                                    continue;
                                }
                                switch (idPizza) {
                                    case 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20:
                                        if (getPizzaFromBDByParameters(idPizza) != null)
                                            order.add(getPizzaFromBDByParameters(idPizza));
                                        printSymbols();
                                        System.out.println("*This pizza is on your order list!");
                                        printSymbols();
                                        break;
                                }
                                break;
                            case 2:
                                printSymbols();
                                if (totalSum() == 0) {
                                    System.out.println("*Your shopping basket is empty...");
                                    printSymbols();
                                    break;
                                }
                                System.out.println("-This is your shopping cart: ");
                                printOrders();
                                printSymbols();
                                System.out.println("*Choose what are you want to do: ");
                                System.out.println("1) Delete something from order || 2) Count total sum from order || 3) Back");
                                choice = scanner.nextInt();
                                switch (choice) {
                                    case 1:
                                        printSymbols();
                                        System.out.print("*Enter id pizza you want to delete: ");
                                        int idPizzaToDelete = scanner.nextInt();
                                        if (deleteObjectFromOrder(idPizzaToDelete) == null) {
                                            System.out.println("*Enter correct id of Pizza...");
                                            printSymbols();
                                            continue;
                                        } else {
                                            System.out.println("*This pizza has been removed from your order....");
                                            printSymbols();
                                            break;
                                        }
                                    case 2:
                                        printSymbols();
                                        if (totalSum() == 0) {
                                            System.out.println("*Your shopping basket is empty...");
                                            printSymbols();
                                            continue;
                                        }
                                        System.out.println("*Total amount of your order: " + totalSum());
                                        printSymbols();
                                        break;
                                    case 3:
                                        break;
                                }
                            case 3:
                                break;
                        }
                        break;
                    case 2:
                        OrdersOfAllTime clientOrders = new OrdersOfAllTime(getOrdersFromDB(clientLog));
                        printSymbols();
                        if (clientOrders.getOrders() == null) {
                            System.out.println("You haven't made any orders yet");
                            printSymbols();
                            continue;
                        }
                        System.out.println("*****************************Your orders for all time*****************************");
                        clientOrders.printAllOrders();
                        printSymbols();
                        break;
                    case 3:
                        //Тут нище міняв
                        printSymbols();
                        if (totalSum() == 0) {
                            System.out.println("*Your shopping basket is empty...");
                            System.out.println("*To make an order you need to order at least 1 pizza!");
                            printSymbols();
                            continue;
                        }
                        System.out.println("You ordered: ");
                        printOrders();
                        printSymbols();
                        double distance = realizeAnOrder("50-001", clientLog.getAddress().getPostalCode());
                        double costDelivery = distance * 2;
                        double sumOfOrderAndDelivery = costDelivery + totalSum();
                        System.out.println("*The distance is: " + new DecimalFormat("#0.00").format(distance) + " km, and the delivery cost is : " + new DecimalFormat("#0.00").format(costDelivery) + " PLN");
                        System.out.println("*You need to pay: " + new DecimalFormat("#0.00").format(sumOfOrderAndDelivery) + " PLN");
                        System.out.println("*Thank you for order.");
                        if (addAnOrderToDB(clientLog)) {
                            return;
                        }
                        break;
                    case 4:
                        printSymbols();
                        System.out.println("*Exit...");
                        printSymbols();
                        return;
                }
            }
        }
    }

    public static void printSymbols() {
        System.out.println("***************************************************************************************************************************************************************************************************************");
    }
}
