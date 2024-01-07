package org.oleksii.admin.promotional_code_for_admin;

import java.time.LocalDateTime;

public class Promo {
    private int id;
    private String name;
    private int discount;
    private String description;
    private LocalDateTime last_modified_date;
    private LocalDateTime end_date;
    private boolean is_active;

    public Promo() {
    }

    public Promo(String name, int discount, String description, LocalDateTime last_modified_date, LocalDateTime end_date, boolean is_active) {
        this.name = name;
        this.discount = discount;
        this.description = description;
        this.last_modified_date = last_modified_date;
        this.end_date = end_date;
        this.is_active = is_active;
    }

    public Promo(int id, String name, int discount, String description, LocalDateTime last_modified_date, LocalDateTime end_date, boolean is_active) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.description = description;
        this.last_modified_date = last_modified_date;
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

    public LocalDateTime getLast_modified_date() {
        return last_modified_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public boolean isIs_active() {
        return is_active;
    }
}
