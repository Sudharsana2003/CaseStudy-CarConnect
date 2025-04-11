package com.carconnect.authentication;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    // For hashing passwords
    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    // For verifying passwords
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
