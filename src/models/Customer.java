package models;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private String address;
    private String password;

    public Customer(int customerId, String name, String email, String phone, 
                    String licenseNumber, String address, String password) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.address = address;
        this.password = password;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
