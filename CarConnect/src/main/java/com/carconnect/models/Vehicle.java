package com.carconnect.models;

public class Vehicle {
    private int vehicleID;
    private String model;
    private String make;
    private int year;
    private String registrationNumber;
    private boolean availability;
    private double dailyRate;

    public Vehicle(int vehicleID, String model, String make, int year, String registrationNumber,
            boolean availability, double dailyRate) {
        this.vehicleID = vehicleID;
        this.model = model;
        this.make = make;
        this.year = year;
        this.registrationNumber = registrationNumber;
        this.availability = availability;
        this.dailyRate = dailyRate;
    }

    // Getters and setters
    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleID=" + vehicleID +
                ", model='" + model + '\'' +
                ", make='" + make + '\'' +
                ", year=" + year +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", availability=" + availability +
                ", dailyRate=" + dailyRate +
                '}';
    }
}