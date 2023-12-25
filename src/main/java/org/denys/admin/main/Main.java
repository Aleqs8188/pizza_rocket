package org.denys.admin.main;

import org.denys.admin.super_users.chief_administrator.SuperAdmin;
import org.denys.admin.super_users.default_administrator.Admin;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static org.denys.admin.databaseAccessors.AdminDatabaseAccessor.*;
import static org.denys.admin.databaseAccessors.AdminDatabaseAccessor.getAdminFromDB;

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
                System.err.println("Incorrect value, try again...");
                continue;
            }
            switch (choice) {
                case 1:
                    printSymbols();
                    Admin adminLog = null;
                    System.out.println("|||||||||||LOGIN TO ADMIN||||||||||||");
                    System.out.print("Username: ");
                    String usernameLogin = reader.readLine();
                    adminLog = getAdminFromDB(usernameLogin);
                    if (adminLog == null) {
                        System.err.println("There is no administrator with this username...");
                        continue;
                    }
                    System.out.print("Password: ");
                    String passwordLogin = reader.readLine();
                    if (!BCrypt.checkpw(passwordLogin, adminLog.getHashedPassword())) {
                        System.err.println("The password is not correct...");
                        continue;
                    } else {
                        System.out.println("Vse chetko");
                    }
                    break;
                case 2:
                    printSymbols();
                    SuperAdmin superAdmin = new SuperAdmin();
                    System.out.println("|||||||||||LOGIN TO CHIEF ADMIN||||||||||||");
                    System.out.print("Username: ");
                    String usernameChiefAdmin = reader.readLine();
                    System.out.print("Password: ");
                    String passwordChiefAdmin = reader.readLine();
                    if (!superAdmin.getPASSWORD().equals(usernameChiefAdmin) && !superAdmin.getPASSWORD().equals(passwordChiefAdmin)) {
                        System.err.println("Are you really sick? Have you really forgotten your username and password?");
                        printSymbols();
                        continue;
                    }
                    printSymbols();
                    System.out.print("(1) Managing Administrators || (2) Managing Pizzas || (3) Managing Promos ---| ");
                    choice = scanner.nextInt();
                    if (choice > 5 /* rewrite 5*/) {
                        System.err.println("Incorrect value, try again...");
                        continue;
                    }
                    switch (choice) {
                        case 1:
                            printSymbols();
                            //Выводим всех администраторов
                            System.out.print("(1) Add new administrator || (2) Delete administrator ---| ");
                            choice = scanner.nextInt();
                            if (choice > 2 /* rewrite 2*/) {
                                System.err.println("Incorrect value, try again...");
                                continue;
                            }
                            switch (choice) {
                                case 1:
                                    printSymbols();
                                    int idNewAdmin = getAdminsFromDB().size();
                                    System.out.print("*Name: ");
                                    String nameNewAdmin = reader.readLine();
                                    System.out.print("*Surname: ");
                                    String surNameNewAdmin = reader.readLine();
                                    //Проверка на такого же узернейма
                                    System.out.print("*Username: ");
                                    String userNameNewAdmin = reader.readLine();
                                    System.out.print("*Password: ");
                                    String passwordNewAdmin = reader.readLine();

                                    String salt = BCrypt.gensalt();
                                    String hashedPassword = BCrypt.hashpw(passwordNewAdmin, salt);
                                    addAdminToDB(new Admin(idNewAdmin + 1, nameNewAdmin, surNameNewAdmin, userNameNewAdmin, hashedPassword, salt));

                                    printSymbols();
                                    System.out.println("*New admin added*");
                                    // Add admin
                                    break;
                                case 2:
                                    // Delete Admin
                                    break;
                            }
                            break;
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

    public static void printSymbols() {
        System.out.println("***************************************************************************************************************************************************************************************************************");
    }
}


