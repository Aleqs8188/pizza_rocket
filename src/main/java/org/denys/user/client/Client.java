package org.denys.user.client;

import org.denys.user.info.Address;
import org.denys.user.info.PaymentInfo;

import java.util.Objects;

public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Address address;
    private PaymentInfo paymentInfo;

    public Client(int id, String firstName, String lastName, String phoneNumber, String email, Address address, PaymentInfo paymentInfo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.paymentInfo = paymentInfo;
    }

    public Client() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address.toString() +
                ", paymentInfo=" + paymentInfo.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(phoneNumber, client.phoneNumber) && Objects.equals(email, client.email) && Objects.equals(address, client.address) && Objects.equals(paymentInfo, client.paymentInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phoneNumber, email, address, paymentInfo);
    }
}
