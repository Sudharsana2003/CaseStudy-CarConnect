package com.carconnect.reports;

import com.carconnect.exceptions.CustomerNotFoundException;
import com.carconnect.exceptions.DatabaseConnectionException;
import com.carconnect.models.Reservation;
import com.carconnect.models.Vehicle;
import com.carconnect.services.ReservationService;
import com.carconnect.services.VehicleServiceImpl;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    private VehicleServiceImpl vehicleService;
    private ReservationService reservationService;

    public ReportGenerator() {
        this.vehicleService = new VehicleServiceImpl();
        this.reservationService = new ReservationService();
    }

    // Method to generate vehicle report
    public void generateVehicleReport() throws DatabaseConnectionException, IOException {
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles(); // Retrieve available vehicles
        if (availableVehicles == null || availableVehicles.isEmpty()) {
            throw new DatabaseConnectionException("No available vehicles found.");
        }

        try (FileWriter writer = new FileWriter("VehicleReport.csv")) {
            // Writing headers to the report
            writer.write("VehicleID,Model,Make,Year,RegistrationNumber,Availability,DailyRate\n");

            // Writing vehicle data to the report
            for (Vehicle vehicle : availableVehicles) {
                writer.write(vehicle.getVehicleID() + "," +
                        vehicle.getModel() + "," +
                        vehicle.getMake() + "," +
                        vehicle.getYear() + "," +
                        vehicle.getRegistrationNumber() + "," +
                        vehicle.isAvailability() + "," +
                        vehicle.getDailyRate() + "\n");
            }
            System.out.println("Vehicle report generated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing the vehicle report: " + e.getMessage());
            throw e; // Propagate the exception
        }
    }

    // Method to generate a reservation report for all customers
    public void generateReservationReport() throws DatabaseConnectionException, IOException {
        List<Reservation> reservations = reservationService.getAllReservations(); // Get all reservations
        if (reservations == null || reservations.isEmpty()) {
            throw new DatabaseConnectionException("No reservations found.");
        }

        try (FileWriter writer = new FileWriter("ReservationReport.csv")) {
            // Writing headers to the report
            writer.write("ReservationID,CustomerID,VehicleID,StartDate,EndDate,TotalCost,Status\n");

            // Writing reservation data to the report
            for (Reservation reservation : reservations) {
                writer.write(reservation.getReservationID() + "," +
                        reservation.getCustomerID() + "," +
                        reservation.getVehicleID() + "," +
                        reservation.getStartDate() + "," +
                        reservation.getEndDate() + "," +
                        reservation.getTotalCost() + "," +
                        reservation.getStatus() + "\n");
            }
            System.out.println("Reservation report generated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing the reservation report: " + e.getMessage());
            throw e; // Propagate the exception
        }
    }

    // Method to generate a reservation report for a specific customer
    public void generateReservationReportForCustomer(int customerId) throws CustomerNotFoundException, IOException {
        List<Reservation> reservations = reservationService.getReservationsByCustomerId(customerId); // Get reservations
                                                                                                     // for a specific
                                                                                                     // customer
        if (reservations == null || reservations.isEmpty()) {
            throw new CustomerNotFoundException("No reservations found for customer ID: " + customerId);
        }

        try (FileWriter writer = new FileWriter("Customer_" + customerId + "_ReservationReport.csv")) {
            // Writing headers to the report
            writer.write("ReservationID,CustomerID,VehicleID,StartDate,EndDate,TotalCost,Status\n");

            // Writing reservation data to the report
            for (Reservation reservation : reservations) {
                writer.write(reservation.getReservationID() + "," +
                        reservation.getCustomerID() + "," +
                        reservation.getVehicleID() + "," +
                        reservation.getStartDate() + "," +
                        reservation.getEndDate() + "," +
                        reservation.getTotalCost() + "," +
                        reservation.getStatus() + "\n");
            }
            System.out.println("Customer Reservation report generated successfully for Customer ID: " + customerId);
        } catch (IOException e) {
            System.out.println("Error writing the reservation report for customer: " + e.getMessage());
            throw e;
        }
    }
}