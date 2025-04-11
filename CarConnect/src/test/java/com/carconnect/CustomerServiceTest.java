package com.carconnect;

import com.carconnect.exceptions.CustomerNotFoundException;
import com.carconnect.models.Customer;
import com.carconnect.services.CustomerService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CustomerServiceTest {

    private final CustomerService customerService = new CustomerService();

    @Test
    public void testInvalidUsernameAuthentication() {
        String invalidUsername = "nonexistent_user";

        Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerByUsername(invalidUsername);
        });
    }

    @Test
    public void testUpdateCustomer() {
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1); // ensure this ID exists in DB
        existingCustomer.setFirstName("UpdatedFirstName");
        existingCustomer.setLastName("UpdatedLastName");
        existingCustomer.setEmail("updated@example.com");
        existingCustomer.setPhoneNumber("1234567890");
        existingCustomer.setAddress("123 MG Road, Mumbai, Maharashtra");
        existingCustomer.setUsername("updatedUsername");
        existingCustomer.setPassword("updatedPassword");
        existingCustomer.setRegistrationDate(new Date(System.currentTimeMillis()));

        assertDoesNotThrow(() -> {
            customerService.updateCustomer(existingCustomer);
        });
    }
}
