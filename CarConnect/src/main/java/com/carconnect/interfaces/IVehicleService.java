package com.carconnect.interfaces;

import com.carconnect.exceptions.InvalidInputException;
import com.carconnect.exceptions.VehicleNotFoundException; // Import VehicleNotFoundException
import com.carconnect.exceptions.DatabaseConnectionException; // Import DatabaseConnectionException
import com.carconnect.models.Vehicle;
import java.util.List;

public interface IVehicleService {
    Vehicle getVehicleById(int vehicleId)
            throws VehicleNotFoundException, InvalidInputException, DatabaseConnectionException; // Add
                                                                                                 // VehicleNotFoundException

    List<Vehicle> getAvailableVehicles() throws DatabaseConnectionException;

    void addVehicle(Vehicle vehicle) throws InvalidInputException, DatabaseConnectionException;

    void updateVehicle(Vehicle vehicle)
            throws VehicleNotFoundException, InvalidInputException, DatabaseConnectionException; // Add
                                                                                                 // VehicleNotFoundException

    void removeVehicle(int vehicleId)
            throws VehicleNotFoundException, InvalidInputException, DatabaseConnectionException; // Add
                                                                                                 // VehicleNotFoundException
}