package com.example.store.Admin;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AddAdminDocument {

    public static void main(String[] args) {
        // Connect to MongoDB server
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> adminsCollection = database.getCollection("Admins");

            // Create a new admin document
            String adminUsername = "Admin";
            String adminPassword = "KMSHardPassword";

            // Generate a random salt for the admin
            String adminSalt = generateSalt();

            // Combine password with salt and hash using SHA-256
            String hashedAdminPassword = hashPassword(adminPassword, adminSalt);

            // Insert the document into the collection
            Document newAdminDocument = new Document()
                    .append("username", adminUsername)
                    .append("password", hashedAdminPassword)
                    .append("salt", adminSalt);

            adminsCollection.insertOne(newAdminDocument);

            System.out.println("Admin document added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateSalt() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
