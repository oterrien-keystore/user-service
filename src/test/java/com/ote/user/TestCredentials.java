package com.ote.user;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.MessageDigest;

public class TestCredentials {

    @Test
    public void test() {

        String password = "password";

        System.out.println(encryptPassword(password));

    }

    private static String encryptPassword(String password) {

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));

            return new BigInteger(1, crypt.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
