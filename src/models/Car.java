package models;

import java.io.Serializable;

public class Car implements Serializable {
    private static final long serialVersionUID = 1L;
    private int carId;
    private String brand;
    private String model;
    private String carType; // Sedan, SUV, Sports, Convertible
    private double pricePerDay;
    private boolean available;
    private String licensePlate;
    private String color;
    private int year;

    public Car(int carId, String brand, String model, String carType, double pricePerDay, 
               String licensePlate, String color, int year) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.carType = carType;
        this.pricePerDay = pricePerDay;
        this.available = true;
        this.licensePlate = licensePlate;
        this.color = color;
        this.year = year;
    }

    // Getters and Setters
    public int getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCarType() {
        return carType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getColor() {
        return color;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return brand + " " + model + " (" + year + ") - " + carType;
    }

    public String getFullDetails() {
        return brand + " " + model + " | Type: " + carType + " | Year: " + year + 
               " | Color: " + color + " | ₹" + pricePerDay + "/day";
    }
}
