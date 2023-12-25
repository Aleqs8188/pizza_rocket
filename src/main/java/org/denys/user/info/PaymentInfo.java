package org.denys.user.info;

public class PaymentInfo {
    private String creditCardNumber;
    private Address billingAddress;

    public PaymentInfo(String creditCardNumber, Address billingAddress) {
        this.creditCardNumber = creditCardNumber;
        this.billingAddress = billingAddress;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "creditCardNumber='" + creditCardNumber + '\'' +
                ", billingAddress=" + billingAddress +
                '}';
    }
}
