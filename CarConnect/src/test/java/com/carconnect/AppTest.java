package com.carconnect;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @BeforeAll
    static void setup() {
        System.out.println("Setting up tests...");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Cleaning up after tests...");
    }

    @Test
    void testApp() {
        assertTrue(true, "This test should always pass");
    }
}
