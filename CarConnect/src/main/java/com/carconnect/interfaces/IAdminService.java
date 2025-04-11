package com.carconnect.interfaces;

import com.carconnect.models.Admin;
import java.sql.SQLException;
import com.carconnect.exceptions.AdminNotFoundException;

public interface IAdminService {
    Admin getAdminById(int adminId) throws SQLException, AdminNotFoundException;

    Admin getAdminByUsername(String username) throws SQLException;

    void registerAdmin(Admin admin) throws SQLException;

    void updateAdmin(Admin admin) throws SQLException;

    void deleteAdmin(int adminId) throws SQLException;
}
