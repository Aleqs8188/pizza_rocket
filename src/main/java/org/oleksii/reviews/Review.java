package org.oleksii.reviews;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private String rating;
    private String description;

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
