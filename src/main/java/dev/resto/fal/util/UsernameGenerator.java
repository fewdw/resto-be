package dev.resto.fal.util;

import java.util.Random;

public class UsernameGenerator {

    // Define the character set: letters (lowercase and uppercase) and digits
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Define the desired username length
    private static final int USERNAME_LENGTH = 8;

    public static String generateRandomUsername() {
        Random random = new Random();
        StringBuilder username = new StringBuilder(USERNAME_LENGTH);

        // Randomly select characters from the charset to create the username
        for (int i = 0; i < USERNAME_LENGTH; i++) {
            int index = random.nextInt(CHARSET.length());
            username.append(CHARSET.charAt(index));
        }

        return username.toString();
    }
}
