package com.carconnect.exceptions;

public class VehicleNotFoundException extends Exception {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}