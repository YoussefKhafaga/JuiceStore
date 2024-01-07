package com.example.store;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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

public class Workers {
    private String username;
    private String Password;

    public Workers(String username, String password) {
        this.username = username;
        Password = password;
    }

    public boolean addWorker(String username, String password)
    {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> adminsCollection = database.getCollection("Workers");

            // Check if an admin with the same username already exists
            Document existingAdmin = adminsCollection.find(Filters.eq("username", username)).first();

            if (existingAdmin != null) {
                // Admin with the same username already exists, handle accordingly
                createAlertBox("الموظف بأسم " + existingAdmin.getString("username") + " موجود بالفعل", "تنبيه", Alert.AlertType.WARNING);
            } else {
                // Generate a random salt for the admin
                String userSalt = generateSalt();

                // Combine password with salt and hash using SHA-256
                String hashedUserPassword = hashPassword(password, userSalt);

                // Insert the document into the collection
                Document newAdminDocument = new Document()
                        .append("username", username)
                        .append("password", hashedUserPassword)
                        .append("salt", userSalt);

                adminsCollection.insertOne(newAdminDocument);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createAlertBox(String msg, String type, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    public boolean authenticateWorker(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> workersCollection = database.getCollection("Workers");

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
