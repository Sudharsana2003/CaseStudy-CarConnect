package com.carconnect;

import com.carconnect.authentication.PasswordHasher;
//Exceptions
import com.carconnect.exceptions.AdminNotFoundException;
import com.carconnect.exceptions.CustomerNotFoundException;
import com.carconnect.exceptions.InvalidInputException;
import com.carconnect.exceptions.ReservationException;
import com.carconnect.exceptions.VehicleNotFoundException;
import com.carconnect.exceptions.DatabaseConnectionException;

//Models
import com.carconnect.models.Admin;
import com.carconnect.models.Customer;
import com.carconnect.models.Reservation;
import com.carconnect.models.Vehicle;
import com.carconnect.reports.ReportGenerator;
//Services - Implementation
import com.carconnect.services.AdminService;
import com.carconnect.services.CustomerService;
import com.carconnect.services.VehicleServiceImpl;
import com.carconnect.services.ReservationService;

//Others
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.sql.Timestamp; // Import Timestamp

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdminService adminService = new AdminService();
        CustomerService customerService = new CustomerService();
        VehicleServiceImpl vehicleService = new VehicleServiceImpl();
        ReservationService reservationService = new ReservationService();
        ReportGenerator reportGenerator = new ReportGenerator();

        while (true) {
            System.out.println("------------------Welcome to Sudharsana CarConnect-----------------");
            System.out.println("Login as:");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Exit CarConnect");
            System.out.print("Enter your choice (1, 2, or 3): ");
            int loginChoice = scanner.nextInt();
            scanner.nextLine();

            if (loginChoice == 3) {
                System.out.println("---------------Thank you for using Sudharsana CarConnect--------------");
                break;
            }

            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            boolean isAuthenticated = false;
            String role = "";
            int loggedInCustomerId = -1;

            try {
                if (loginChoice == 1) {
                    isAuthenticated = customerService.authenticateCustomer(username, password);
                    if (isAuthenticated) {
                        role = "customer";
                        try {
                            loggedInCustomerId = customerService.getCustomerIdByUsername(username);
                        } catch (Exception e) {
                            System.out.println("Error retrieving customer ID: " + e.getMessage());
                        }
                    }
                } else if (loginChoice == 2) {
                    isAuthenticated = adminService.authenticateAdmin(username, password);
                    if (isAuthenticated) {
                        role = "admin";
                    }
                } else {
                    System.out.println("Invalid login choice.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Error during authentication: " + e.getMessage());
                continue;
            }

            if (!isAuthenticated) {
                System.out.println("Login failed. Invalid username or password.");
                continue;
            }

            System.out.println("Login successful as " + role + ": " + username);

            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("\n---------- Sudharsana CarConnect Menu ----------");
                if (role.equals("admin")) {
                    System.out.println("1. Admin Management");
                    System.out.println("2. Vehicle Management");
                    System.out.println("3. Customer Management");
                    System.out.println("4. Reservation Management");
                    System.out.println("5. Generate Vehicle Report");
                    System.out.println("6. Generate All Reservations Report");
                    System.out.println("7. Generate Reservation Report by Customer ID");
                    System.out.println("8. Logout"); // Changed Exit to Logout
                } else {
                    System.out.println("1. View My Profile");
                    System.out.println("2. Make Reservation");
                    System.out.println("3. View My Reservations");
                    System.out.println("4. Cancel My Reservation");
                    System.out.println("5. View All Vehicles");
                    System.out.println("6. Update My Profile");
                    System.out.println("7. Delete My Account");
                    System.out.println("8. View Profile by ID");
                    System.out.println("9. View Profile by Username");
                    System.out.println("10. View Reservation by ID");
                    System.out.println("11. Update My Reservation");
                    System.out.println("12. Generate My Reservation Report");
                    System.out.println("13. Logout"); // Changed Exit to Logout
                }

                System.out.print("Enter your choice: ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                try {
                    if (role.equals("admin")) {
                        switch (choice) {
                            case 1:
                                adminManagement(scanner, adminService);
                                break;
                            case 2:
                                vehicleManagement(scanner, vehicleService);
                                break;
                            case 3:
                                customerManagement(scanner, customerService);
                                break;
                            case 4:
                                reservationManagement(scanner, reservationService);
                                break;
                            case 5:
                                reportGenerator.generateVehicleReport();
                                break;
                            case 6:
                                reportGenerator.generateReservationReport();
                                break;
                            case 7:
                                System.out.print("Enter Customer ID to generate report: ");
                                int customerIdForReport = Integer.parseInt(scanner.nextLine());
                                reportGenerator.generateReservationReportForCustomer(customerIdForReport);
                                break;
                            case 8:
                                System.out.println("Logged out successfully.");
                                loggedIn = false; // Break out of the user's session loop
                                break;
                            default:
                                System.out.println("Invalid choice. Please select from 1 to 8.");
                        }
                    } else {
                        switch (choice) {
                            case 1:
                                getCustomerByUsername(scanner, customerService);
                                break;
                            case 2:
                                createReservation(scanner, reservationService, vehicleService, loggedInCustomerId);
                                break;
                            case 3:
                                getReservationsByCustomerId(scanner, reservationService, loggedInCustomerId);
                                break;
                            case 4:
                                cancelReservation(scanner, reservationService);
                                break;
                            case 5:
                                getAvailableVehicles(vehicleService);
                                break;
                            case 6:
                                updateCustomer(scanner, customerService);
                                break;
                            case 7:
                                deleteCustomer(scanner, customerService);
                                break;
                            case 8:
                                getCustomerById(scanner, customerService);
                                break;
                            case 9:
                                getCustomerByUsername(scanner, customerService);
                                break;
                            case 10:
                                getReservationById(scanner, reservationService);
                                break;
                            case 11:
                                updateReservation(scanner, reservationService);
                                break;
                            case 12:
                                if (loggedInCustomerId != -1) {
                                    reportGenerator.generateReservationReportForCustomer(loggedInCustomerId);
                                } else {
                                    System.out.println("Error: Customer ID not found for generating report.");
                                }
                                break;
                            case 13:
                                System.out.println("Logged out successfully.");
                                loggedIn = false; // Break out of the user's session loop
                                break;
                            default:
                                System.out.println("Invalid choice. Please select from 1 to 13.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        scanner.close();
    }

    // 1. ADMIN MANAGEMENT
    private static void adminManagement(Scanner scanner, AdminService adminService) throws SQLException {
        while (true) {
            System.out.println("\nAdmin Management System");
            System.out.println("1. Register Admin");
            System.out.println("2. Get Admin by ID");
            System.out.println("3. Get Admin by Username");
            System.out.println("4. Update Admin");
            System.out.println("5. Delete Admin");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerAdmin(scanner, adminService);
                    break;
                case 2:
                    getAdminById(scanner, adminService);
                    break;
                case 3:
                    getAdminByUsername(scanner, adminService);
                    break;
                case 4:
                    updateAdmin(scanner, adminService);
                    break;
                case 5:
                    deleteAdmin(scanner, adminService);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void vehicleManagement(Scanner scanner, VehicleServiceImpl vehicleService)
            throws InvalidInputException, DatabaseConnectionException, VehicleNotFoundException {
        while (true) {
            System.out.println("\nVehicle Management System");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Get Vehicle by ID");
            System.out.println("3. Get Available Vehicles");
            System.out.println("4. Update Vehicle");
            System.out.println("5. Remove Vehicle");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        addVehicle(scanner, vehicleService);
                        break;
                    case 2:
                        getVehicleById(scanner, vehicleService);
                        break;
                    case 3:
                        getAvailableVehicles(vehicleService);
                        break;
                    case 4:
                        updateVehicle(scanner, vehicleService);
                        break;
                    case 5:
                        removeVehicle(scanner, vehicleService);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InvalidInputException | DatabaseConnectionException | VehicleNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void registerAdmin(Scanner scanner, AdminService adminService) throws SQLException {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String hashedPassword = PasswordHasher.hashPassword(password);

        System.out.print("Enter Role: ");
        String role = scanner.nextLine();
        Date joinDate = new Date(System.currentTimeMillis());

        Admin newAdmin = new Admin(0, firstName, lastName, email, phoneNumber, username, hashedPassword, role,
                joinDate);
        adminService.registerAdmin(newAdmin);
        System.out.println("Admin registered successfully.");
    }

    private static void getAdminById(Scanner scanner, AdminService adminService) {
        System.out.print("Enter Admin ID: ");
        int adminId = scanner.nextInt();
        try {
            Admin adminById = adminService.getAdminById(adminId);
            displayAdminDetails(adminById);
        } catch (AdminNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error occurred. Please try again.");
            e.printStackTrace();
        }
        scanner.nextLine();
    }

    private static void getAdminByUsername(Scanner scanner, AdminService adminService) throws SQLException {
        System.out.print("Enter Username: ");
        String searchUsername = scanner.nextLine();
        Admin adminByUsername = adminService.getAdminByUsername(searchUsername);
        if (adminByUsername != null) {
            displayAdminDetails(adminByUsername);
        } else {
            System.out.println("Admin not found.");
        }
    }

    private static void updateAdmin(Scanner scanner, AdminService adminService) throws SQLException {
        System.out.print("Enter Admin ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();
        try {
            Admin existingAdmin = adminService.getAdminById(updateId);
            if (existingAdmin == null) {
                System.out.println("Admin not found.");
                return;
            }
            System.out.print("Enter First Name (" + existingAdmin.getFirstName() + "): ");
            String updatedFirstName = scanner.nextLine().trim();
            System.out.print("Enter Last Name (" + existingAdmin.getLastName() + "): ");
            String updatedLastName = scanner.nextLine().trim();
            System.out.print("Enter Email (" + existingAdmin.getEmail() + "): ");
            String updatedEmail = scanner.nextLine().trim();
            System.out.print("Enter Phone Number (" + existingAdmin.getPhoneNumber() + "): ");
            String updatedPhoneNumber = scanner.nextLine().trim();
            System.out.print("Enter Username (" + existingAdmin.getUsername() + "): ");
            String updatedUsername = scanner.nextLine().trim();
            System.out.print("Enter Password (Leave blank to keep unchanged): ");
            String updatedPassword = scanner.nextLine().trim();
            System.out.print("Enter Role (" + existingAdmin.getRole() + "): ");
            String updatedRole = scanner.nextLine().trim();

            Admin updatedAdmin = new Admin(
                    updateId,
                    updatedFirstName.isEmpty() ? existingAdmin.getFirstName() : updatedFirstName,
                    updatedLastName.isEmpty() ? existingAdmin.getLastName() : updatedLastName,
                    updatedEmail.isEmpty() ? existingAdmin.getEmail() : updatedEmail,
                    updatedPhoneNumber.isEmpty() ? existingAdmin.getPhoneNumber() : updatedPhoneNumber,
                    updatedUsername.isEmpty() ? existingAdmin.getUsername() : updatedUsername,
                    updatedPassword.isEmpty() ? existingAdmin.getPassword() : updatedPassword,
                    updatedRole.isEmpty() ? existingAdmin.getRole() : updatedRole,
                    existingAdmin.getJoinDate());

            adminService.updateAdmin(updatedAdmin);
            System.out.println("Admin updated successfully.");
        } catch (AdminNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteAdmin(Scanner scanner, AdminService adminService) throws SQLException {
        System.out.print("Enter Admin ID to delete: ");
        int deleteId = scanner.nextInt();
        adminService.deleteAdmin(deleteId);
        System.out.println("Admin deleted successfully.");
        scanner.nextLine();
    }

    private static void displayAdminDetails(Admin admin) {
        System.out.println("\nAdmin Details:");
        System.out.println("ID: " + admin.getAdminId());
        System.out.println("Name: " + admin.getFirstName() + " " + admin.getLastName());
        System.out.println("Email: " + admin.getEmail());
        System.out.println("Phone: " + admin.getPhoneNumber());
        System.out.println("Username: " + admin.getUsername());
        System.out.println("Role: " + admin.getRole());
        System.out.println("Join Date: " + admin.getJoinDate());
    }

    // 2. VEHICLE MANAGEMENT

    private static void addVehicle(Scanner scanner, VehicleServiceImpl vehicleService)
            throws InvalidInputException, DatabaseConnectionException {
        System.out.print("Enter Vehicle Model: ");
        String model = scanner.nextLine();
        System.out.print("Enter Vehicle Make: ");
        String make = scanner.nextLine();
        System.out.print("Enter Vehicle Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Registration Number: ");
        String registrationNumber = scanner.nextLine();
        System.out.print("Enter Daily Rate: ");
        double dailyRate = scanner.nextDouble();
        scanner.nextLine();

        Vehicle newVehicle = new Vehicle(0, model, make, year, registrationNumber, true, dailyRate);
        vehicleService.addVehicle(newVehicle);
        System.out.println("Vehicle added successfully.");
    }

    private static void getVehicleById(Scanner scanner, VehicleServiceImpl vehicleService)
            throws InvalidInputException, DatabaseConnectionException, VehicleNotFoundException {
        System.out.print("Enter Vehicle ID: ");
        int vehicleId = scanner.nextInt();
        scanner.nextLine();
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        System.out.println(vehicle);
    }

    private static void updateVehicle(Scanner scanner, VehicleServiceImpl vehicleService)
            throws InvalidInputException, DatabaseConnectionException, VehicleNotFoundException {
        System.out.print("Enter Vehicle ID to update: ");
        int vehicleId = scanner.nextInt();
        scanner.nextLine();
        Vehicle existingVehicle = vehicleService.getVehicleById(vehicleId);
        if (existingVehicle == null) {
            System.out.println("Vehicle not found");
            return;
        }
        System.out.print("Enter Vehicle Model (" + existingVehicle.getModel() + "): ");
        String model = scanner.nextLine().trim();
        System.out.print("Enter Vehicle Make (" + existingVehicle.getMake() + "): ");
        String make = scanner.nextLine().trim();
        System.out.print("Enter Vehicle Year (" + existingVehicle.getYear() + "): ");
        String yearStr = scanner.nextLine().trim();
        int year = yearStr.isEmpty() ? existingVehicle.getYear() : Integer.parseInt(yearStr);
        System.out.print("Enter Registration Number (" + existingVehicle.getRegistrationNumber() + "): ");
        String registrationNumber = scanner.nextLine().trim();
        System.out.print("Enter Daily Rate (" + existingVehicle.getDailyRate() + "): ");
        String dailyRateStr = scanner.nextLine().trim();
        double dailyRate = dailyRateStr.isEmpty() ? existingVehicle.getDailyRate() : Double.parseDouble(dailyRateStr);

        Vehicle updatedVehicle = new Vehicle(
                vehicleId,
                model.isEmpty() ? existingVehicle.getModel() : model,
                make.isEmpty() ? existingVehicle.getMake() : make,
                year,
                registrationNumber.isEmpty() ? existingVehicle.getRegistrationNumber() : registrationNumber,
                true,
                dailyRate);
        vehicleService.updateVehicle(updatedVehicle);
        System.out.println("Vehicle updated successfully");
    }

    private static void removeVehicle(Scanner scanner, VehicleServiceImpl vehicleService)
            throws InvalidInputException, DatabaseConnectionException, VehicleNotFoundException {
        System.out.print("Enter Vehicle ID to remove: ");
        int vehicleId = scanner.nextInt();
        vehicleService.removeVehicle(vehicleId);
        System.out.println("Vehicle removed successfully");
        scanner.nextLine();
    }

    // 3. CUSTOMER MANAGEMENT

    private static void customerManagement(Scanner scanner, CustomerService customerService)
            throws InvalidInputException, DatabaseConnectionException, CustomerNotFoundException, SQLException {
        while (true) {
            System.out.println("\nCustomer Management System");
            System.out.println("1. Register Customer");
            System.out.println("2. Get Customer by ID");
            System.out.println("3. Get Customer by Username");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerCustomer(scanner, customerService);
                    break;
                case 2:
                    getCustomerById(scanner, customerService);
                    break;
                case 3:
                    getCustomerByUsername(scanner, customerService);
                    break;
                case 4:
                    updateCustomer(scanner, customerService);
                    break;
                case 5:
                    deleteCustomer(scanner, customerService);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerCustomer(Scanner scanner, CustomerService customerService)
            throws InvalidInputException, DatabaseConnectionException, SQLException {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Hash the password using PasswordHasher
        String hashedPassword = PasswordHasher.hashPassword(password);

        // Use the hashed password to create the Customer object
        Date registrationDate = new Date(System.currentTimeMillis());
        Customer newCustomer = new Customer(0, firstName, lastName, email, phoneNumber, address, username,
                hashedPassword,
                registrationDate);

        // Register the customer
        customerService.registerCustomer(newCustomer);
        System.out.println("Customer registered successfully.");
    }

    // 4. RESERVATION MANAGEMENT
    public static void reservationManagement(Scanner scanner, ReservationService reservationService)
            throws ReservationException, CustomerNotFoundException {
        while (true) {
            System.out.println("\nReservation Management System");
            System.out.println("1. Get Reservation by ID");
            System.out.println("2. Get Reservations by Customer ID");
            System.out.println("3. Create Reservation");
            System.out.println("4. Update Reservation");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    getReservationById(scanner, reservationService);
                    break;
                case 2:
                    getReservationsByCustomerId(scanner, reservationService);
                    break;
                case 3:
                    createReservation(scanner, reservationService);
                    break;
                case 4:
                    updateReservation(scanner, reservationService);
                    break;
                case 5:
                    cancelReservation(scanner, reservationService);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void getReservationsByCustomerId(Scanner scanner, ReservationService reservationService)
            throws CustomerNotFoundException {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        List<Reservation> reservations = reservationService.getReservationsByCustomerId(customerId);
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    private static void createReservation(Scanner scanner, ReservationService reservationService)
            throws ReservationException {
        System.out.print("Enter Reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Vehicle ID: ");
        int vehicleId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        Date startDate = Date.valueOf(scanner.nextLine());
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        Date endDate = Date.valueOf(scanner.nextLine());
        System.out.print("Enter Total Cost: ");
        double totalCost = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Status: ");
        String status = scanner.nextLine();

        Reservation reservation = new Reservation(reservationId, customerId, vehicleId, startDate, endDate, totalCost,
                status);
        reservationService.createReservation(reservation);
    }

    // REPORT GENERATOR MANAGEMENT

    static void reportGenerationManagement(Scanner scanner, ReportGenerator reportGenerator) {
        System.out.println("\n--- Report Generation Menu ---");
        System.out.println("1. Generate Vehicle Report");
        System.out.println("2. Generate All Reservations Report");
        System.out.println("3. Generate Reservation Report by Customer ID");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int reportChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        try {
            switch (reportChoice) {
                case 1:
                    reportGenerator.generateVehicleReport();
                    break;
                case 2:
                    reportGenerator.generateReservationReport();
                    break;
                case 3:
                    System.out.print("Enter Customer ID for Reservation Report: ");
                    int customerIdForReport = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    reportGenerator.generateReservationReportForCustomer(customerIdForReport);
                    break;
                case 4:
                    System.out.println("Returning to the main menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 4.");
            }
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    static void getCustomerByUsername(Scanner scanner, CustomerService customerService) {
        System.out.print("Enter username to view profile: ");
        String username = scanner.nextLine();
        try {
            System.out.println(customerService.getCustomerByUsername(username));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void createReservation(Scanner scanner, ReservationService reservationService,
            VehicleServiceImpl vehicleService, int customerId) {
        System.out.println("--- Create New Reservation ---");

        // 1. Display available vehicles
        try {
            System.out.println("--- Available Vehicles ---");
            vehicleService.getAvailableVehicles().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching available vehicles: " + e.getMessage());
            return;
        }

        // 2. Get vehicle ID from the customer
        System.out.print("Enter the Vehicle ID you want to reserve: ");
        int vehicleId;
        try {
            vehicleId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Vehicle ID format.");
            return;
        }

        // 3. Get start date from the customer
        System.out.print("Enter the start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        Date startDate;
        try {
            startDate = Date.valueOf(startDateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        // 4. Get end date from the customer
        System.out.print("Enter the end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();
        Date endDate;
        try {
            endDate = Date.valueOf(endDateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        // 5. Validate dates
        if (endDate.before(startDate) || endDate.equals(startDate)) {
            System.out.println("End date must be after the start date.");
            return;
        }

        // 6. Check vehicle availability and details
        Vehicle vehicle = null;
        try {
            vehicle = vehicleService.getVehicleById(vehicleId);
            if (vehicle == null) {
                System.out.println("Vehicle with ID " + vehicleId + " not found.");
                return;
            }
            if (!vehicle.isAvailability()) {
                System.out.println("Vehicle with ID " + vehicleId + " is currently not available.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error checking vehicle details: " + e.getMessage());
            return;
        }

        // 7. Calculate total cost
        double totalCost = 0.0;
        if (vehicle != null) {
            long timeDifference = endDate.getTime() - startDate.getTime();
            long rentalDaysInMillis = timeDifference + (24 * 60 * 60 * 1000);
            int rentalDays = (int) (rentalDaysInMillis / (1000 * 60 * 60 * 24));
            if (rentalDays <= 0) {
                System.out.println("Rental duration must be at least one day.");
                return;
            }
            totalCost = vehicle.getDailyRate() * rentalDays;

            // 8. Ask user for the desired status
            System.out.println("\n--- Reservation Details ---");
            System.out.println("Vehicle Model: " + vehicle.getMake() + " " + vehicle.getModel());
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
            System.out.println("Rental Days: " + rentalDays);
            System.out.printf("Total Cost: %.2f%n", totalCost);
            System.out.println("Available Reservation Statuses: Pending, Confirmed, Completed");
            System.out.print("Enter the desired reservation status: ");
            String status = scanner.nextLine().trim();

            // 9. Validate the entered status against the allowed ENUM values
            if (!status.equals("Pending") && !status.equals("Confirmed") && !status.equals("Completed")) {
                System.out.println("Invalid status entered. Reservation will be set to Pending.");
                status = "Pending"; // Default to Pending if invalid input
            }

            // 10. Ask user for confirmation of the reservation
            System.out.print("Confirm reservation with status '" + status + "'? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
                Reservation newReservation = new Reservation(0, customerId, vehicleId, startDate, endDate, totalCost,
                        status);

                // 11. Create the reservation
                try {
                    reservationService.createReservation(newReservation);
                    System.out.println("Reservation created successfully with status '" + status
                            + "'. Reservation ID will be assigned.");
                } catch (Exception e) {
                    System.out.println("Error creating reservation: " + e.getMessage());
                }
            } else {
                System.out.println("Reservation cancelled by the user.");
            }
        }
    }

    static void getReservationsByCustomerId(Scanner scanner, ReservationService reservationService, int customerId) {
        try {
            System.out.println("--- Your Reservations ---");
            java.util.List<Reservation> reservations = reservationService.getReservationsByCustomerId(customerId);
            if (reservations.isEmpty()) {
                System.out.println("No reservations found for your account.");
            } else {
                reservations.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error fetching your reservations: " + e.getMessage());
        }
    }

    static void cancelReservation(Scanner scanner, ReservationService reservationService) {
        System.out.print("Enter Reservation ID to cancel: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        try {
            reservationService.cancelReservation(reservationId);
            System.out.println("Reservation " + reservationId + " cancelled successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void getAvailableVehicles(VehicleServiceImpl vehicleService) {
        try {
            System.out.println("--- Available Vehicles ---");
            vehicleService.getAvailableVehicles().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateCustomer(Scanner scanner, CustomerService customerService) {
        System.out.println("--- Update My Profile ---");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        try {
            Customer existingCustomer = customerService.getCustomerByUsername(username);
            if (existingCustomer == null) {
                System.out.println("Customer with username '" + username + "' not found.");
                return;
            }

            Customer updatedCustomer = new Customer();
            updatedCustomer.setCustomerId(existingCustomer.getCustomerId()); // Keep the original ID
            updatedCustomer.setUsername(username); // Keep the original username

            System.out.print("Enter new First Name (leave blank to keep '" + existingCustomer.getFirstName() + "'): ");
            String firstName = scanner.nextLine();
            updatedCustomer.setFirstName(firstName.isEmpty() ? existingCustomer.getFirstName() : firstName);

            System.out.print("Enter new Last Name (leave blank to keep '" + existingCustomer.getLastName() + "'): ");
            String lastName = scanner.nextLine();
            updatedCustomer.setLastName(lastName.isEmpty() ? existingCustomer.getLastName() : lastName);

            System.out.print("Enter new Email (leave blank to keep '" + existingCustomer.getEmail() + "'): ");
            String email = scanner.nextLine();
            updatedCustomer.setEmail(email.isEmpty() ? existingCustomer.getEmail() : email);

            System.out.print(
                    "Enter new Phone Number (leave blank to keep '" + existingCustomer.getPhoneNumber() + "'): ");
            String phoneNumber = scanner.nextLine();
            updatedCustomer.setPhoneNumber(phoneNumber.isEmpty() ? existingCustomer.getPhoneNumber() : phoneNumber);

            System.out.print("Enter new Address (leave blank to keep '" + existingCustomer.getAddress() + "'): ");
            String address = scanner.nextLine();
            updatedCustomer.setAddress(address.isEmpty() ? existingCustomer.getAddress() : address);

            System.out.print("Enter new Password (leave blank to keep your current password): ");
            String password = scanner.nextLine();
            updatedCustomer.setPassword(password.isEmpty() ? existingCustomer.getPassword() : password); // Consider
                                                                                                         // hashing here

            updatedCustomer.setRegistrationDate(existingCustomer.getRegistrationDate()); // Keep original registration
                                                                                         // date

            customerService.updateCustomer(updatedCustomer);
            System.out.println("Profile updated successfully.");

        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }

    static void deleteCustomer(Scanner scanner, CustomerService customerService) {
        System.out.println("--- Delete My Account ---");
        System.out.print("Enter your username to confirm deletion: ");
        String usernameToDelete = scanner.nextLine();
        System.out.print("Are you sure you want to delete your account? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes")) {
            try {
                Customer customerToDelete = customerService.getCustomerByUsername(usernameToDelete);
                if (customerToDelete != null) {
                    int customerIdToDelete = customerToDelete.getCustomerId();
                    customerService.deleteCustomer(customerIdToDelete); // Call the void method
                    System.out.println("Account with username '" + usernameToDelete + "' (ID: " + customerIdToDelete
                            + ") deleted successfully. You will be logged out.");
                } else {
                    System.out.println("Customer with username '" + usernameToDelete + "' not found.");
                }
            } catch (CustomerNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            } catch (DatabaseConnectionException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        } else {
            System.out.println("Account deletion cancelled.");
        }
    }

    static void getCustomerById(Scanner scanner, CustomerService customerService) {
        System.out.print("Enter Customer ID to view profile: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        try {
            System.out.println(customerService.getCustomerById(customerId));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void getReservationById(Scanner scanner, ReservationService reservationService) {
        System.out.print("Enter Reservation ID to view: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        try {
            System.out.println(reservationService.getReservationById(reservationId));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateReservation(Scanner scanner, ReservationService reservationService) {
        System.out.print("Enter the Reservation ID you want to update: ");
        int reservationId;
        try {
            reservationId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Reservation ID format.");
            return;
        }

        try {
            Reservation existingReservation = reservationService.getReservationById(reservationId);
            if (existingReservation == null) {
                System.out.println("Reservation with ID " + reservationId + " not found.");
                return;
            }

            Reservation updatedReservation = new Reservation(
                    reservationId,
                    existingReservation.getCustomerID(),
                    existingReservation.getVehicleID(),
                    existingReservation.getStartDate(),
                    existingReservation.getEndDate(),
                    existingReservation.getTotalCost(),
                    existingReservation.getStatus());

            System.out.println("Current Reservation Details:");
            System.out.println(existingReservation);

            System.out.print("Enter new Start Date (YYYY-MM-DD HH:MM:SS, leave blank to keep '"
                    + existingReservation.getStartDate() + "'): ");
            String startDateStr = scanner.nextLine();
            java.sql.Date startDate = new java.sql.Date(existingReservation.getStartDate().getTime()); // Convert to
                                                                                                       // java.sql.Date
            if (!startDateStr.isEmpty()) {
                try {
                    startDate = new java.sql.Date(Timestamp.valueOf(startDateStr).getTime()); // Use java.sql.Date
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date/time format. Please use<ctrl3348>-MM-DD HH:MM:SS.");
                    return;
                }
            }
            updatedReservation.setStartDate(startDate);

            System.out.print("Enter new End Date (YYYY-MM-DD, leave blank to keep '" + existingReservation.getEndDate()
                    + "'): ");
            String endDateStr = scanner.nextLine();
            java.sql.Date endDate = new java.sql.Date(existingReservation.getEndDate().getTime()); // Convert to
                                                                                                   // java.sql.Date
            if (!endDateStr.isEmpty()) {
                try {
                    endDate = Date.valueOf(endDateStr);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please use<ctrl3348>-MM-DD.");
                    return;
                }
            }
            updatedReservation.setEndDate(endDate);

            // Validate dates
            if (updatedReservation.getEndDate().before(updatedReservation.getStartDate())
                    || updatedReservation.getEndDate().equals(updatedReservation.getStartDate())) {
                System.out.println("End date must be after the start date.");
                return;
            }

            // Recalculate total cost based on new dates
            VehicleServiceImpl vehicleService = new VehicleServiceImpl(); // Consider how to access this service
                                                                          // properly
            try {
                Vehicle vehicle = vehicleService.getVehicleById(existingReservation.getVehicleID());
                if (vehicle != null) {
                    long timeDifference = updatedReservation.getEndDate().getTime()
                            - updatedReservation.getStartDate().getTime();
                    long rentalDaysInMillis = timeDifference + (24 * 60 * 60 * 1000 - 1); // Add almost a day
                    int rentalDays = (int) (rentalDaysInMillis / (1000 * 60 * 60 * 24));
                    if (rentalDays <= 0) {
                        System.out.println("Rental duration must be at least one day.");
                        return;
                    }
                    updatedReservation.setTotalCost(vehicle.getDailyRate() * rentalDays);
                    System.out.printf("New Total Cost: %.2f%n", updatedReservation.getTotalCost());
                } else {
                    System.out.println("Error: Vehicle details not found for cost recalculation.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error fetching vehicle details: " + e.getMessage());
                return;
            }

            System.out.print("Enter new Status (Pending, Confirmed, Completed, leave blank to keep '"
                    + existingReservation.getStatus() + "'): ");
            String status = scanner.nextLine().trim();
            if (!status.isEmpty()) {
                if (status.equals("Pending") || status.equals("Confirmed") || status.equals("Completed")) {
                    updatedReservation.setStatus(status);
                } else {
                    System.out.println("Invalid status. Please use Pending, Confirmed, or Completed.");
                    return;
                }
            } else {
                updatedReservation.setStatus(existingReservation.getStatus());
            }

            reservationService.updateReservation(updatedReservation);
            System.out.println("Reservation with ID " + reservationId + " updated successfully.");

        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating reservation: " + e.getMessage());
        }
    }

}
