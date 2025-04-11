package com.carconnect.database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseContext.getConnection();
            if (conn != null) {
                System.out.println("Database Connection Successful!");
                conn.close();
            } else {
                System.out.println("Database Connection Failed!");
            }
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
