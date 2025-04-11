package com.carconnect.services;

import com.carconnect.interfaces.IAdminService;
import com.carconnect.models.Admin;
import com.carconnect.authentication.PasswordHasher;
import com.carconnect.database.DatabaseContext;
import com.carconnect.exceptions.*;

import java.sql.*;

public class AdminService implements IAdminService {

    @Override
    public Admin getAdminById(int adminId) throws SQLException, AdminNotFoundException {
        String query = "SELECT * FROM Admin WHERE AdminID = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, adminId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs);
                }
            }
        }
        throw new AdminNotFoundException("Admin with ID " + adminId + " not found.");
    }

    @Override
    public Admin getAdminByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Admin WHERE Username = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void registerAdmin(Admin admin) throws SQLException {
        String query = "INSERT INTO Admin (FirstName, LastName, Email, PhoneNumber, Username, Password, Role, JoinDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            setAdminStatement(stmt, admin);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateAdmin(Admin admin) throws SQLException {
        String query = "UPDATE Admin SET FirstName=?, LastName=?, Email=?, PhoneNumber=?, Username=?, Password=?, Role=?, JoinDate=? WHERE AdminID=?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            setAdminStatement(stmt, admin);
            stmt.setInt(9, admin.getAdminId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAdmin(int adminId) throws SQLException {
        String query = "DELETE FROM Admin WHERE AdminID = ?";
        try (Connection conn = DatabaseContext.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, adminId);
            stmt.executeUpdate();
        }
    }

    private Admin mapAdmin(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getInt("AdminID"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email"),
                rs.getString("PhoneNumber"),
                rs.getString("Username"),
                rs.getString("Password"),
                rs.getString("Role"),
                rs.getDate("JoinDate"));
    }

    private void setAdminStatement(PreparedStatement stmt, Admin admin) throws SQLException {
        stmt.setString(1, admin.getFirstName());
        stmt.setString(2, admin.getLastName());
        stmt.setString(3, admin.getEmail());
        stmt.setString(4, admin.getPhoneNumber());
        stmt.setString(5, admin.getUsername());
        stmt.setString(6, admin.getPassword());
        stmt.setString(7, admin.getRole());
        stmt.setDate(8, new java.sql.Date(admin.getJoinDate().getTime()));
    }

public boolean authenticateAdmin(String username, String password) {
    try {
        Admin admin = getAdminByUsername(username);
        return admin != null && PasswordHasher.checkPassword(password, admin.getPassword());
    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
        return false;
    }
}


}
