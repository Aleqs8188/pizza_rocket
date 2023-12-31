package org.oleksii.admin.super_users.default_administrator;

public class Admin {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String salt;
    private String hashedPassword;
    private String secret_code;

    public Admin(int id, String name, String surname, String username, String hashedPassword, String salt) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }

    public Admin(int id, String name, String surname, String username, String hashedPassword, String salt, String secret_code) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.secret_code = secret_code;
    }

    public Admin(String name, String surname, String username, String hashedPassword, String salt) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }

    public Admin(int id, String name, String surname, String username) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
    }

    public Admin(int id) {
        this.id = id;
    }

    public Admin() {
    }

    public String getSalt() {
        return salt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
