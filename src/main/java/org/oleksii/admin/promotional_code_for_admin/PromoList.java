package org.oleksii.admin.promotional_code_for_admin;

import org.oleksii.enums.ConsoleColor;

import java.util.ArrayList;

import static org.oleksii.admin.databaseAccessors.PromoDatabaseAccessorForAdmin.get_promos_from_db;

public class PromoList extends Promo {
    private final ArrayList<Promo> promoArrayList = get_promos_from_db();

    public void print_all_promo() {
        int counter = 1;
        for (Promo promo : promoArrayList) {
            System.out.println(ConsoleColor.BLINK.getCode() + ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+------------------+------------+------------------------------+------------------------------+----------------+--------------------------------------------------------");
            System.out.println("| ID |       Name       | Discount % |      Last modified date      |        End promo date        |  Active promo  |");
            System.out.println("+----+------------------+------------+------------------------------+------------------------------+----------------+--------------------------------------------------------");
            String coloredPromo = String.valueOf(promo.isIs_active());
            if (coloredPromo.equals("false")) {
                coloredPromo = ConsoleColor.RED.getCode() + coloredPromo;
            }
            String formattedString = String.format(ConsoleColor.MAGENTA.getCode() + ConsoleColor.BOLD.getCode() +
                            "|%3s | %-16s | %-10s | %-28s | %-29s| %-14s |",
                    counter,
                    promo.getName(),
                    promo.getDiscount() + "%",
                    promo.getLast_modified_date(),
                    promo.getEnd_date(),
                    coloredPromo);
            counter++;
            String formattedDescription = String.format(ConsoleColor.MAGENTA.getCode() + ConsoleColor.BOLD.getCode() +
                            "|%13s %s",
                    "Description:",
                    promo.getDescription()
            );
            System.out.println(formattedString);
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+-------------------------------------------------------------------------------------------------------------------+--------------------------------------------------------" + ConsoleColor.RESET.getCode());
            System.out.println(formattedDescription);
            System.out.println(ConsoleColor.GREEN.getCode() + ConsoleColor.BOLD.getCode() + "+----+------------------+------------+------------------------------+------------------------------+----------------+--------------------------------------------------------" + ConsoleColor.RESET.getCode());
        }
    }
}
