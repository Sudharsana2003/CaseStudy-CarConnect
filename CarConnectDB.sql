#CASE STUDY - CarConnect

#Creation of Database
create database CarConnectDB;

#Using the created Database
Use CarConnectDB;

#1. Creation of Customer Table
create table Customer(
CustomerID int primary key auto_increment,
FirstName varchar(30) not null,
LastName varchar(30) not null,
Email varchar(200) unique not null check (Email regexp '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
PhoneNumber varchar(15) unique not null,
Address varchar(300) not null,
Username Varchar(20) unique not null,
Password varchar(300) not null, #can be hashed in java code logic for security simce 2 users can have same password
RegistrationDate date
);

#2. Creation of Vehicle Table
create table Vehicle(
VehicleID int primary key auto_increment,
Model varchar(30) not null,
Make varchar(30) not null,
Year year not null,
Color varchar(30),
RegistrationNumber varchar(30) unique not null,
Availability boolean default true,
DailyRate decimal(10,2) not null
);

#3. Creation of Reservation Table
create table Reservation(
ReservationID int primary key auto_increment,
CustomerID int not null,
VehicleID int not null,
StartDate datetime not null,
EndDate date not null,
TotalCost decimal(10,2) not null,
Status enum('Pending','Confirmed','Completed') not null default 'Pending',
foreign key(CustomerID)references Customer(CustomerID) on delete cascade,
foreign key(VehicleID) references Vehicle(VehicleID) on delete cascade,
check(EndDate>StartDate)
);

#4. Creation of Admin table

CREATE TABLE admin (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(30) NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    Email VARCHAR(200) UNIQUE NOT NULL CHECK (Email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
    PhoneNumber VARCHAR(15) UNIQUE NOT NULL,
    UserName VARCHAR(30) UNIQUE NOT NULL,
    Password VARCHAR(300) NOT NULL,
    Role ENUM(
        'Super Admin', 
        'Fleet Manager', 
        'Operations Manager',
        'Customer Support', 
        'Finance Manager', 
        'Sales Manager',
        'HR Manager'
    ) NOT NULL,
    JoinDate DATE NOT NULL
);



#2. Insertion Of Sample Records

#2.1 Customers
INSERT INTO Customer (FirstName, LastName, Email, PhoneNumber, Address, Username, Password, RegistrationDate)
VALUES 
('Amit', 'Sharma', 'amitsharma@email.com', '9876543210', '101 MG Road, Mumbai, Maharashtra', 'amitsharma', 'hashedpassword1', '2024-01-05'),
('Priya', 'Verma', 'priyaverma@email.com', '9765432109', '202 Rajaji St, Chennai, Tamil Nadu', 'priyaverma', 'hashedpassword2', '2024-02-10'),
('Rahul', 'Nair', 'rahulnair@email.com', '9654321098', '305 MG Road, Bangalore, Karnataka', 'rahulnair', 'hashedpassword3', '2024-03-15'),
('Sneha', 'Patel', 'snehapatel@email.com', '9543210987', '509 Park St, Ahmedabad, Gujarat', 'snehapatel', 'hashedpassword4', '2024-04-20'),
('Vikram', 'Singh', 'vikramsingh@email.com', '9432109876', '710 Mall Road, New Delhi', 'vikramsingh', 'hashedpassword5', '2024-05-05'),
('Ananya', 'Reddy', 'ananyareddy@email.com', '9321098765', '88 Jubilee Hills, Hyderabad, Telangana', 'ananyareddy', 'hashedpassword6', '2024-06-12'),
('Rajesh', 'Kumar', 'rajeshkumar@email.com', '9210987654', '303 Nehru Nagar, Pune, Maharashtra', 'rajeshkumar', 'hashedpassword7', '2024-07-18'),
('Meera', 'Joshi', 'meerajoshi@email.com', '9109876543', '45 Civil Lines, Jaipur, Rajasthan', 'meerajoshi', 'hashedpassword8', '2024-08-22'),
('Suresh', 'Yadav', 'sureshyadav@email.com', '9098765432', '98 Anna Nagar, Coimbatore, Tamil Nadu', 'sureshyadav', 'hashedpassword9', '2024-09-30'),
('Kavya', 'Mehta', 'kavyamehta@email.com', '8987654321', '12 Residency Rd, Kolkata, West Bengal', 'kavyamehta', 'hashedpassword10', '2024-10-15');

#2.2 Vehicle
INSERT INTO Vehicle (Model, Make, Year, Color, RegistrationNumber, Availability, DailyRate)
VALUES 
('Swift', 'Maruti Suzuki', 2022, 'White', 'MH01AB1234', true, 1200.00),
('City', 'Honda', 2021, 'Black', 'TN07CD5678', true, 1500.00),
('i20', 'Hyundai', 2020, 'Blue', 'KA05EF9012', false, 1100.00),
('Scorpio', 'Mahindra', 2023, 'Red', 'DL02GH3456', true, 1800.00),
('Fortuner', 'Toyota', 2022, 'Silver', 'GJ03IJ7890', false, 2500.00),
('Nexon', 'Tata', 2021, 'Grey', 'TS04KL4321', true, 1300.00),
('Verna', 'Hyundai', 2020, 'Maroon', 'RJ05MN8765', true, 1400.00),
('Baleno', 'Maruti Suzuki', 2022, 'White', 'WB06OP6543', false, 1250.00),
('Innova', 'Toyota', 2019, 'Black', 'KL07QR3210', true, 2000.00),
('Ertiga', 'Maruti Suzuki', 2023, 'Blue', 'AP08ST9876', true, 1600.00);

#2.3 Reservation

INSERT INTO Reservation (CustomerID, VehicleID, StartDate, EndDate, TotalCost, Status)  
VALUES  
(1, 1, '2024-01-10 09:00:00', '2024-01-15', 6000.00, 'Confirmed'),  
(2, 2, '2024-02-12 10:00:00', '2024-02-14', 3000.00, 'Pending'),  
(3, 3, '2024-03-05 08:00:00', '2024-03-07', 2200.00, 'Completed'),  
(4, 4, '2024-04-08 11:00:00', '2024-04-12', 7200.00, 'Pending'),  
(5, 5, '2024-05-15 09:30:00', '2024-05-20', 12500.00, 'Confirmed'),  
(6, 6, '2024-06-22 12:00:00', '2024-06-25', 3900.00, 'Pending'),  
(7, 7, '2024-07-01 07:00:00', '2024-07-03', 2800.00, 'Completed'),  
(8, 8, '2024-08-10 10:30:00', '2024-08-12', 2500.00, 'Confirmed'),  
(9, 9, '2024-09-18 06:00:00', '2024-09-22', 8000.00, 'Pending'),  
(10, 10, '2024-10-25 08:00:00', '2024-10-30', 9600.00, 'Confirmed');  


ALTER TABLE admin MODIFY Role ENUM(
    'Super Admin', 
    'Fleet Manager', 
    'Operations Manager',
    'Customer Support', 
    'Finance Manager', 
    'Sales Manager',
    'HR Manager', 
    'Customer Support Head', 
    'Security Head', 
    'Tech Lead', 
    'Quality Manager'
) NOT NULL;

ALTER TABLE admin AUTO_INCREMENT = 1;


#2.4 Admin

INSERT INTO Admin (FirstName, LastName, Email, PhoneNumber, UserName, Password, Role, JoinDate)
VALUES 
('Arjun', 'Iyer', 'arjuni@email.com', '9876543211', 'arjun_admin', 'hashedadminpass1', 'Super Admin', '2023-01-01'),
('Neha', 'Kohli', 'nehakohli@email.com', '9765432108', 'neha_ops', 'hashedadminpass2', 'Operations Manager', '2023-02-10'),
('Sandeep', 'Menon', 'sandeepm@email.com', '9654321097', 'sandeep_fleet', 'hashedadminpass3', 'Fleet Manager', '2023-03-15'),
('Ritika', 'Das', 'ritikadas@email.com', '9543210986', 'ritika_sales', 'hashedadminpass4', 'Sales Manager', '2023-04-20'),
('Varun', 'Chopra', 'varunchopra@email.com', '9432109875', 'varun_support', 'hashedadminpass5', 'Customer Support Head', '2023-05-25'),
('Pooja', 'Singh', 'poojasingh@email.com', '9321098764', 'pooja_finance', 'hashedadminpass6', 'Finance Manager', '2023-06-30'),
('Ankit', 'Sharma', 'ankitsharma@email.com', '9210987653', 'ankit_hr', 'hashedadminpass7', 'HR Manager', '2023-07-15'),
('Meenal', 'Gupta', 'meenalgupta@email.com', '9109876542', 'meenal_security', 'hashedadminpass8', 'Security Head', '2023-08-10'),
('Nitin', 'Rao', 'nitinrao@email.com', '9098765431', 'nitin_tech', 'hashedadminpass9', 'Tech Lead', '2023-09-05'),
('Swati', 'Verma', 'swativerma@email.com', '8987654320', 'swati_quality', 'hashedadminpass10', 'Quality Manager', '2023-10-20');


