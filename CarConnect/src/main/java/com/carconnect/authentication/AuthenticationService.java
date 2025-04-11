package com.carconnect.authentication;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.carconnect.database.DatabaseContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {

    /**
     * Authenticates a user by checking both the admin and customer tables.
     *
     * @param username the input username
     * @param password the input plain-text password
     * @return true if authentication is successful; false otherwise
     */
    public boolean authenticate(String username, String password) {
        return checkInTable("admin", username, password) || checkInTable("customer", username, password);
    }

    /**
     * Verifies a user's password against the stored hashed password in the
     * specified table.
     *
     * @param table    the table to check (admin or customer)
     * @param username the username
     * @param password the plain-text password to verify
     * @return true if verified successfully; false otherwise
     */
    private boolean checkInTable(String table, String username, String password) {
        String query = "SELECT Password FROM " + table + " WHERE UserName = ?";

        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("Password");

                // Verify password using BCrypt
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash);
                return result.verified;
            }

        } catch (SQLException e) {
            System.err.println("Error authenticating from table '" + table + "': " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

}
