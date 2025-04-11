package com.carconnect.services;

import com.carconnect.models.Customer;
import com.carconnect.interfaces.ICustomerService;
import com.carconnect.exceptions.CustomerNotFoundException;
import com.carconnect.exceptions.InvalidInputException;
import com.carconnect.exceptions.DatabaseConnectionException;
import com.carconnect.authentication.PasswordHasher;
import com.carconnect.database.DatabaseContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerService implements ICustomerService {

    @Override
    public Customer getCustomerById(int customerId)
            throws CustomerNotFoundException, InvalidInputException, DatabaseConnectionException {
        if (customerId <= 0) {
            throw new InvalidInputException("Invalid customer ID provided.");
        }
        String sql = "SELECT * FROM customer WHERE customerId = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("customerId"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("email"),
                            rs.getString("phoneNumber"),
                            rs.getString("address"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getDate("registrationDate"));
                } else {
                    throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    @Override
    public Customer getCustomerByUsername(String username)
            throws CustomerNotFoundException, DatabaseConnectionException {
        String sql = "SELECT * FROM customer WHERE username = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("customerId"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("email"),
                            rs.getString("phoneNumber"),
                            rs.getString("address"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getDate("registrationDate"));
                } else {
                    throw new CustomerNotFoundException("Customer with username " + username + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    @Override
    public void registerCustomer(Customer customerData) throws InvalidInputException, DatabaseConnectionException {
        if (customerData.getFirstName().isEmpty() || customerData.getLastName().isEmpty() ||
                customerData.getEmail().isEmpty() || customerData.getPhoneNumber().isEmpty() ||
                customerData.getAddress().isEmpty() || customerData.getUsername().isEmpty()
                || customerData.getPassword().isEmpty()) {
            throw new InvalidInputException("Invalid customer data provided.");
        }

        String sql = "INSERT INTO customer (firstName, lastName, email, phoneNumber, address, username, password, registrationDate) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerData.getFirstName());
            pstmt.setString(2, customerData.getLastName());
            pstmt.setString(3, customerData.getEmail());
            pstmt.setString(4, customerData.getPhoneNumber());
            pstmt.setString(5, customerData.getAddress());
            pstmt.setString(6, customerData.getUsername());
            pstmt.setString(7, customerData.getPassword());
            pstmt.setDate(8, customerData.getRegistrationDate());
            pstmt.executeUpdate();
            // System.out.println("Customer registered successfully.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    @Override
    public void updateCustomer(Customer customer)
            throws SQLException, CustomerNotFoundException, InvalidInputException, DatabaseConnectionException {

        Customer existingCustomer = getCustomerById(customer.getCustomerId());
        if (existingCustomer == null) {
            throw new CustomerNotFoundException(
                    "Customer with ID " + customer.getCustomerId() + " not found for update.");
        }

        String newPassword = customer.getPassword();
        String hashedPasswordToStore = existingCustomer.getPassword();

        if (!newPassword.isEmpty() && !PasswordHasher.checkPassword(newPassword, existingCustomer.getPassword())) {
            hashedPasswordToStore = PasswordHasher.hashPassword(newPassword);
            customer.setPassword(hashedPasswordToStore);
        } else {

            customer.setPassword(hashedPasswordToStore);
        }

        String sql = "UPDATE customer SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ?, Password = ? WHERE CustomerID = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getPassword()); // Now this will be the hashed password (or the existing hash)
            pstmt.setInt(7, customer.getCustomerId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CustomerNotFoundException(
                        "Customer with ID " + customer.getCustomerId() + " not found for update.");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error during customer update: " + e.getMessage());
        }
    }

    @Override
    public void deleteCustomer(int customerId)
            throws CustomerNotFoundException, InvalidInputException, DatabaseConnectionException {
        if (customerId <= 0) {
            throw new InvalidInputException("Invalid customer ID provided for deletion.");
        }

        String sql = "DELETE FROM customer WHERE customerId = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new CustomerNotFoundException("Customer ID not found for deletion.");
            }
            System.out.println("Customer removed successfully.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
    }

    public boolean authenticateCustomer(String username, String password) {
        try {
            Customer customer = getCustomerByUsername(username);
            return customer != null && PasswordHasher.checkPassword(password, customer.getPassword());
        } catch (DatabaseConnectionException | CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public int getCustomerIdByUsername(String username) throws CustomerNotFoundException {
        String sql = "SELECT CustomerID FROM customer WHERE Username = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CustomerID");
                } else {
                    throw new CustomerNotFoundException("Customer with username '" + username + "' not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider more specific exception handling
            throw new RuntimeException("Database error while fetching customer ID.", e);
        }
    }
}
