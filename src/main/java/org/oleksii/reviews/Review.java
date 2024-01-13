package org.oleksii.reviews;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private String rating;
    private String description;
    private int client_id;

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    private LocalDateTime dateTime;

    public Review() {
    }

    public Review(int id, String rating, String description, LocalDateTime localDateTime) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.dateTime = localDateTime;
    }

    public Review(int id, String rating, String description, LocalDateTime localDateTime, int client_id) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.dateTime = localDateTime;
        this.client_id = client_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
