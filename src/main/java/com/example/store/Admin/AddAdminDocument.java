package com.example.store.Admin;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.scene.control.Alert;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AddAdminDocument {

    public static void main(String[] args) {
        // Connect to MongoDB server
    }

    public boolean authenticateWorker(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> workersCollection = database.getCollection("Admins");

            // Find the worker with the given username
            Document workerDocument = workersCollection.find(Filters.eq("username", username)).first();

            if (workerDocument != null) {
                // Worker with the given username found, now check the password
                String storedPasswordHash = workerDocument.getString("password");
                String salt = workerDocument.getString("salt");
                String hashedInputPassword = hashPassword(password, salt);

                // Compare the stored hashed password with the input hashed password
                if (storedPasswordHash.equals(hashedInputPassword)) {
                    // Passwords match, authentication successful
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void addAdmin(String adminUsername, String adminPassword) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> adminsCollection = database.getCollection("Admins");

            // Check if an admin with the same username already exists
            Document existingAdmin = adminsCollection.find(Filters.eq("username", adminUsername)).first();
            if (existingAdmin != null) {
                // Admin with the same username already exists
                /*String storedPassword = existingAdmin.getString("password");
                String adminSalt = existingAdmin.getString("salt");
                String hashedAdminPassword = hashPassword(adminPassword, adminSalt);*/
                createAlertBox("الادمن بأسم " + adminUsername + " موجود بالفعل", "تنبيه", Alert.AlertType.WARNING);

                /*if (hashedAdminPassword.equals(storedPassword)) {
                    // Passwords match, show a message box
                    // Display a message box
                    createAlertBox("الادمن بأسم " + adminUsername + " موجود بالفعل", "تنبيه", Alert.AlertType.WARNING);
                }*/
                }
            else {
                // Generate a random salt for the admin
                String adminSalt1 = generateSalt();
                // Combine password with salt and hash using SHA-256
                String hashedAdminPassword1 = hashPassword(adminPassword, adminSalt1);

                // Insert the document into the collection
                Document newAdminDocument = new Document()
                        .append("username", adminUsername)
                        .append("password", hashedAdminPassword1)
                        .append("salt", adminSalt1);

                adminsCollection.insertOne(newAdminDocument);
                createAlertBox("تم تسجيل الادمن" , "تأكيد", Alert.AlertType.INFORMATION);
            }
            System.out.println("Admin document added successfully!");
            }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createAlertBox(String msg, String type, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
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
