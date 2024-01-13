package org.oleksii;

import org.oleksii.enums.ConsoleColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.oleksii.admin.main.AdminMain.adminMain;
import static org.oleksii.user.main.UserMain.clientMain;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            Thread adminThread = new Thread(() -> {
                try {
                    adminMain();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Thread clientThread = new Thread(() -> {
                try {
                    clientMain();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            System.out.print(ConsoleColor.BOLD.getCode() + ConsoleColor.MAGENTA.getCode() + "Are you a client? (1-Yes  2-No): " + ConsoleColor.RESET.getCode());
            String choice = reader.readLine().toLowerCase();

            if (choice.equals("1") || choice.equals("yes")) {
                clientThread.start();
                clientThread.join();
            } else if (choice.equals("2") || choice.equals("no")) {
                adminThread.start();
                adminThread.join();
            } else {
                break;
            }
        }
    }
}
