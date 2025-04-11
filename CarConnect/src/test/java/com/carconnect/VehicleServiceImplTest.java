package com.carconnect;

import com.carconnect.models.Vehicle;
import com.carconnect.services.VehicleServiceImpl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleServiceImplTest {

    private final VehicleServiceImpl vehicleService = new VehicleServiceImpl();

    @Test
    public void testAddVehicle() {
        Vehicle newVehicle = new Vehicle(
                0, // vehicleID (assumed auto-incremented)
                "Swift", // model
                "Maruti", // make
                2022, // year
                "Updated TN01ZZ99991", // registrationNumber
                true, // availability
                850.0 // dailyRate
        );

        assertDoesNotThrow(() -> {
            vehicleService.addVehicle(newVehicle);
        });
    }

    @Test
    public void testUpdateVehicle() {
        Vehicle updatedVehicle = new Vehicle(
                1, // Ensure this ID exists in DB
                "City", // model
                "Honda", // make
                2023, // year
                "TN11YY2222", // registrationNumber
                false, // availability
                950.0 // dailyRate
        );

        assertDoesNotThrow(() -> {
            vehicleService.updateVehicle(updatedVehicle);
        });
    }

    @Test
    public void testGetAvailableVehicles() {
        assertDoesNotThrow(() -> {
            List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
            assertNotNull(availableVehicles);
            System.out.println("Available vehicles count: " + availableVehicles.size());
        });
    }

    @Test
    public void testGetAllVehicles() {
        assertDoesNotThrow(() -> {
            // Assuming you have a method to get all vehicles in your service, else use
            // availableVehicles for demo.
            List<Vehicle> allVehicles = vehicleService.getAvailableVehicles(); // Replace with getAllVehicles() if
                                                                               // available
            assertNotNull(allVehicles);
            System.out.println("Total vehicles fetched: " + allVehicles.size());
        });
    }
}
