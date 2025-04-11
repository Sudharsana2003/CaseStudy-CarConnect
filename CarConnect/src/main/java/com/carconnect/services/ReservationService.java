package com.carconnect.services;

import com.carconnect.database.DatabaseContext;
import com.carconnect.exceptions.CustomerNotFoundException;
import com.carconnect.exceptions.DatabaseConnectionException;
import com.carconnect.exceptions.ReservationException;
import com.carconnect.interfaces.IReservationService;
import com.carconnect.models.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservationService {

    @Override
    public Reservation getReservationById(int reservationId) throws CustomerNotFoundException {
        Reservation reservation = null;
        try (Connection connection = DatabaseContext.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM reservation WHERE ReservationID = ?")) {
            preparedStatement.setInt(1, reservationId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reservation = new Reservation(
                        resultSet.getInt("ReservationID"),
                        resultSet.getInt("CustomerID"),
                        resultSet.getInt("VehicleID"),
                        resultSet.getTimestamp("StartDate"),
                        resultSet.getDate("EndDate"),
                        resultSet.getDouble("TotalCost"),
                        resultSet.getString("Status"));
            } else {
                throw new CustomerNotFoundException("Reservation ID " + reservationId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return reservation;
    }

    @Override
    public List<Reservation> getReservationsByCustomerId(int customerId) throws CustomerNotFoundException {
        List<Reservation> customerReservations = new ArrayList<>();
        try (Connection connection = DatabaseContext.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM reservation WHERE CustomerID = ?")) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customerReservations.add(new Reservation(
                        resultSet.getInt("ReservationID"),
                        resultSet.getInt("CustomerID"),
                        resultSet.getInt("VehicleID"),
                        resultSet.getTimestamp("StartDate"),
                        resultSet.getDate("EndDate"),
                        resultSet.getDouble("TotalCost"),
                        resultSet.getString("Status")));
            }
            if (customerReservations.isEmpty()) {
                throw new CustomerNotFoundException("No reservations found for customer ID: " + customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return customerReservations;
    }

    @Override
    public void createReservation(Reservation reservation) throws ReservationException {
        Connection connection = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet resultSet = null;
        // List<String> existingBookingDetails = new ArrayList<>();

        try {
            connection = DatabaseContext.getConnection();

            // 1. Check for overlapping reservations and retrieve details
            String checkSql = "SELECT StartDate, EndDate FROM reservation " +
                    "WHERE VehicleID = ? AND " +
                    "((StartDate <= ? AND EndDate >= ?) OR (StartDate <= ? AND EndDate >= ?) OR (StartDate >= ? AND EndDate <= ?))";
            checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, reservation.getVehicleID());
            checkStmt.setTimestamp(2, new Timestamp(reservation.getEndDate().getTime())); // New EndDate
            checkStmt.setTimestamp(3, new Timestamp(reservation.getStartDate().getTime())); // New StartDate
            checkStmt.setTimestamp(4, new Timestamp(reservation.getStartDate().getTime())); // New StartDate
            checkStmt.setDate(5, new java.sql.Date(reservation.getEndDate().getTime())); // New EndDate
            checkStmt.setTimestamp(6, new Timestamp(reservation.getStartDate().getTime())); // New StartDate
            checkStmt.setTimestamp(7, new Timestamp(reservation.getEndDate().getTime())); // New EndDate

            resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                do {
                    // Timestamp existingStartDate = resultSet.getTimestamp("StartDate");
                    // Date existingEndDate = resultSet.getDate("EndDate");
                    // existingBookingDetails.add("from " + existingStartDate + " to " +
                    // existingEndDate);
                } while (resultSet.next());

                throw new ReservationException(
                        "Oops..This vehicle was already booked...Consider Booking from the Available Vehicles....");
            }

            // 2. If no overlap, create the new reservation
            String insertSql = "INSERT INTO reservation (CustomerID, VehicleID, StartDate, EndDate, TotalCost, Status) VALUES (?, ?, ?, ?, ?, ?)";
            insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setInt(1, reservation.getCustomerID());
            insertStmt.setInt(2, reservation.getVehicleID());
            insertStmt.setTimestamp(3, new Timestamp(reservation.getStartDate().getTime()));
            insertStmt.setDate(4, new java.sql.Date(reservation.getEndDate().getTime()));
            insertStmt.setDouble(5, reservation.getTotalCost());
            insertStmt.setString(6, reservation.getStatus());
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            throw new ReservationException("Failed to create reservation: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (checkStmt != null)
                    checkStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (insertStmt != null)
                    insertStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateReservation(Reservation reservation) throws CustomerNotFoundException {
        try (Connection connection = DatabaseContext.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE reservation SET CustomerID = ?, VehicleID = ?, StartDate = ?, EndDate = ?, TotalCost = ?, Status = ? WHERE ReservationID = ?")) {
            preparedStatement.setInt(1, reservation.getCustomerID());
            preparedStatement.setInt(2, reservation.getVehicleID());
            preparedStatement.setTimestamp(3, new Timestamp(reservation.getStartDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(reservation.getEndDate().getTime()));
            preparedStatement.setDouble(5, reservation.getTotalCost());
            preparedStatement.setString(6, reservation.getStatus());
            preparedStatement.setInt(7, reservation.getReservationID());
            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Updation done Successfully");

            if (affectedRows == 0) {
                throw new CustomerNotFoundException(
                        "Reservation ID " + reservation.getReservationID() + " not found for update.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelReservation(int reservationId) throws CustomerNotFoundException {
        try (Connection connection = DatabaseContext.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("DELETE FROM reservation WHERE ReservationID = ?")) {
            preparedStatement.setInt(1, reservationId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new CustomerNotFoundException("Reservation ID " + reservationId + " not found for cancellation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservations() throws DatabaseConnectionException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DatabaseContext.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation")) {
            while (resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getInt("ReservationID"),
                        resultSet.getInt("CustomerID"),
                        resultSet.getInt("VehicleID"),
                        resultSet.getTimestamp("StartDate"),
                        resultSet.getDate("EndDate"),
                        resultSet.getDouble("TotalCost"),
                        resultSet.getString("Status")));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error fetching reservations from the database: " + e.getMessage());
        }
        return reservations;
    }
}