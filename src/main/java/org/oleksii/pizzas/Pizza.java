package org.oleksii.pizzas;

import java.sql.Array;

public class Pizza {
    private int id;
    private String name;
    private String description;
    private double price;
    private String size;
    private String ingredients;
    private String type;
    private Array rating;

    public Pizza(int id, String name, String description, double price, String size, String ingredients, String type, Array rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.ingredients = ingredients;
        this.type = type;
        this.rating = rating;
    }

    public Pizza() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Array getRating() {
        return rating;
    }

    public void setRating(Array rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", type='" + type + '\'' +
                ", rating=" + rating +
                '}';
    }
}
