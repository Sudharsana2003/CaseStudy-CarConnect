package com.carconnect.services;

import com.carconnect.database.DatabaseContext;
import com.carconnect.exceptions.DatabaseConnectionException;
import com.carconnect.exceptions.InvalidInputException;
import com.carconnect.exceptions.VehicleNotFoundException;
import com.carconnect.interfaces.IVehicleService;
import com.carconnect.models.Vehicle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements IVehicleService {

    @Override
    public Vehicle getVehicleById(int vehicleId)
            throws VehicleNotFoundException, InvalidInputException, DatabaseConnectionException {
        if (vehicleId <= 0) {
            throw new InvalidInputException("Invalid vehicle ID provided.");
        }
        String sql = "SELECT * FROM vehicle WHERE VehicleID = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                            rs.getInt("VehicleID"),
                            rs.getString("Model"),
                            rs.getString("Make"),
                            rs.getInt("Year"),
                            rs.getString("RegistrationNumber"),
                            rs.getBoolean("Availability"),
                            rs.getDouble("DailyRate"));
                } else {
                    throw new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Vehicle> getAvailableVehicles() throws DatabaseConnectionException {
        List<Vehicle> availableVehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE Availability = 1";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                availableVehicles.add(new Vehicle(
                        rs.getInt("VehicleID"),
                        rs.getString("Model"),
                        rs.getString("Make"),
                        rs.getInt("Year"),
                        rs.getString("RegistrationNumber"),
                        rs.getBoolean("Availability"),
                        rs.getDouble("DailyRate")));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
        return availableVehicles;
    }

    @Override
    public void addVehicle(Vehicle vehicle) throws InvalidInputException, DatabaseConnectionException {
        if (vehicle.getModel().isEmpty() || vehicle.getMake().isEmpty() || vehicle.getYear() <= 0 ||
                vehicle.getRegistrationNumber().isEmpty() || vehicle.getDailyRate() <= 0) {
            throw new InvalidInputException("Invalid vehicle data provided.");
        }
        String sql = "INSERT INTO vehicle (Model, Make, Year, RegistrationNumber, Availability, DailyRate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getModel());
            pstmt.setString(2, vehicle.getMake());
            pstmt.setInt(3, vehicle.getYear());
            pstmt.setString(4, vehicle.getRegistrationNumber());
            pstmt.setBoolean(5, vehicle.isAvailability());
            pstmt.setDouble(6, vehicle.getDailyRate());
            pstmt.executeUpdate();
            // System.out.println("Vehicle added successfully.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    @Override
    public void updateVehicle(Vehicle vehicle)
            throws VehicleNotFoundException, InvalidInputException, DatabaseConnectionException {
        if (vehicle.getVehicleID() <= 0) {
            throw new InvalidInputException("Invalid vehicle ID provided for update.");
        }
        String sql = "UPDATE vehicle SET Model = ?, Make = ?, Year = ?, RegistrationNumber = ?, Availability = ?, DailyRate = ? WHERE VehicleID = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getModel());
            pstmt.setString(2, vehicle.getMake());
            pstmt.setInt(3, vehicle.getYear());
            pstmt.setString(4, vehicle.getRegistrationNumber());
            pstmt.setBoolean(5, vehicle.isAvailability());
            pstmt.setDouble(6, vehicle.getDailyRate());
            pstmt.setInt(7, vehicle.getVehicleID());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new VehicleNotFoundException("Vehicle ID not found for update.");
            }
            // System.out.println("Vehicle updated successfully.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    @Override
    public void removeVehicle(int vehicleId)
            throws VehicleNotFoundException, InvalidInputException, DatabaseConnectionException {
        if (vehicleId <= 0) {
            throw new InvalidInputException("Invalid vehicle ID provided for deletion.");
        }
        String sql = "DELETE FROM vehicle WHERE VehicleID = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new VehicleNotFoundException("Vehicle ID not found for deletion.");
            }
            //System.out.println("Vehicle removed successfully.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    public List<Vehicle> getAllVehicles() throws DatabaseConnectionException {
        List<Vehicle> allVehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";
        try (Connection conn = DatabaseContext.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allVehicles.add(new Vehicle(
                        rs.getInt("VehicleID"),
                        rs.getString("Model"),
                        rs.getString("Make"),
                        rs.getInt("Year"),
                        rs.getString("RegistrationNumber"),
                        rs.getBoolean("Availability"),
                        rs.getDouble("DailyRate")));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error fetching all vehicles: " + e.getMessage());
        }
        return allVehicles;
    }

    public List<Vehicle> getAvailableVehiclesOptimized() throws DatabaseConnectionException {
        List<Vehicle> availableVehicles = new ArrayList<>();
        List<Vehicle> allVehicles = getAllVehicles();
        for (Vehicle vehicle : allVehicles) {
            if (vehicle.isAvailability()) {
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }
}