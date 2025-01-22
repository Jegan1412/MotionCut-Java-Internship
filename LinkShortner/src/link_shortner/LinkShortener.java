package link_shortner;

import java.util.HashMap;
import java.util.Scanner;

public class LinkShortener {

    // HashMaps to store mappings
    private final HashMap<String, String> shortToLong = new HashMap<>();
    private final HashMap<String, String> longToShort = new HashMap<>();
    private final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int shortURLLength = 6;

    // Method to shorten a URL
    public String shortenURL(String longURL) {
        if (longToShort.containsKey(longURL)) {
            return longToShort.get(longURL); // Return existing short URL
        }

        String shortURL;
        do {
            shortURL = generateShortURL();
        } while (shortToLong.containsKey(shortURL)); // Ensure uniqueness

        // Store the mappings
        shortToLong.put(shortURL, longURL);
        longToShort.put(longURL, shortURL);
        return shortURL;
    }

    // Method to expand a short URL
    public String expandURL(String shortURL) {
        return shortToLong.getOrDefault(shortURL, "Error: Short URL not found");
    }

    // Basic hash function to generate a short URL
    private String generateShortURL() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shortURLLength; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    // Command-line interface
    public static void main(String[] args) {
        LinkShortener linkShortener = new LinkShortener();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Link Shortener Application!");
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1) Shorten URL");
            System.out.println("2) Expand URL");
            System.out.println("3) Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.println("Enter the long URL:");
                    String longURL = scanner.nextLine();
                    if (longURL.isEmpty()) {
                        System.out.println("Error: URL cannot be empty!");
                    } else {
                        String shortURL = linkShortener.shortenURL(longURL);
                        System.out.println("Shortened URL: " + shortURL);
                    }
                    break;
                case 2:
                    System.out.println("Enter the short URL:");
                    String shortURL = scanner.nextLine();
                    String expandedURL = linkShortener.expandURL(shortURL);
                    System.out.println("Original URL: " + expandedURL);
                    break;
                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}

