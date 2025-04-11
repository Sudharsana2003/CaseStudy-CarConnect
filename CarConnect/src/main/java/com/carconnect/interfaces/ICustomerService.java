package com.carconnect.interfaces;

import com.carconnect.models.Customer;

import java.sql.SQLException;

import com.carconnect.exceptions.CustomerNotFoundException;
import com.carconnect.exceptions.InvalidInputException;
import com.carconnect.exceptions.DatabaseConnectionException;

public interface ICustomerService {
        Customer getCustomerById(int customerId)
                        throws CustomerNotFoundException, InvalidInputException, DatabaseConnectionException;

        Customer getCustomerByUsername(String username) throws CustomerNotFoundException, DatabaseConnectionException;

        void registerCustomer(Customer customerData) throws InvalidInputException, DatabaseConnectionException;

        void updateCustomer(Customer customer)
                        throws SQLException, CustomerNotFoundException, InvalidInputException,
                        DatabaseConnectionException;

        void deleteCustomer(int customerId)
                        throws CustomerNotFoundException, InvalidInputException, DatabaseConnectionException;
}