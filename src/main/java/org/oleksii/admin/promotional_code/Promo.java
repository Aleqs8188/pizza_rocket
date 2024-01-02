package org.oleksii.admin.promotional_code;

import java.time.LocalDateTime;

public class Promo {
    private int id;
    private String name;
    private int discount;
    private String description;
    private LocalDateTime date_of_creation;
    private LocalDateTime end_date;
    private boolean is_active;

    public Promo() {
    }

    public Promo(String name, int discount, String description, LocalDateTime date_of_creation, LocalDateTime end_date, boolean is_active) {
        this.name = name;
        this.discount = discount;
        this.description = description;
        this.date_of_creation = date_of_creation;
        this.end_date = end_date;
        this.is_active = is_active;
    }

    public Promo(int id, String name, int discount, String description, LocalDateTime date_of_creation, LocalDateTime end_date, boolean is_active) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.description = description;
        this.date_of_creation = date_of_creation;
        this.end_date = end_date;
        this.is_active = is_active;
    }

    public Promo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate_of_creation() {
        return date_of_creation;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public boolean isIs_active() {
        return is_active;
    }
}
