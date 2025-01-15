package dev.resto.fal.util;

public class CountryExtractor {
    public static String getCountry(String address) {
        String[] parts = address.split(", ");
        return parts[parts.length - 1];
    }
}
