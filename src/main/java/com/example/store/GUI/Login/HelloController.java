package com.example.store.GUI.Login;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Base64;

public class HelloController {

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Button startButton;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordTextField;
    //@FXML
    //private ToggleButton showPasswordToggle;
    @FXML
    private Button LoginButton;

    // MongoDB connection details
    private static final String MONGO_DB_URL = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "KhanMariaStore";
    private static final String COLLECTION_NAME = "Admins";

    public void initialize() {
        startButton.setOnAction(event -> switchToSecondView());
        LoginButton.setOnAction(actionEvent -> handleLogin());
        //showPasswordToggle.setOnAction(event -> togglePasswordVisibility());
    }

    private void switchToSecondView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Menu/Menu.fxml"));
            Scene scene = new Scene(loader.load());

            Stage currentStage = (Stage) anchorpane.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Menu");
            currentStage.setResizable(false);
            currentStage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLogin() {
        String enteredUsername = userNameTextField.getText();
        String enteredPassword = passwordTextField.getText();

        // Connect to MongoDB and validate login
        if (validateLogin(enteredUsername, enteredPassword)) {
            switchToSecondView();
        } else {
            showErrorAlert("اسم المستخدم او كلمة المرور غير صحيحة");
        }
    }

    public boolean validateLogin(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_DB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> adminsCollection = database.getCollection(COLLECTION_NAME);

            Document query = new Document("username", username);
            Document adminDocument = adminsCollection.find(query).first();

            if (adminDocument != null) {
                String hashedPassword = adminDocument.getString("password");
                String salt = adminDocument.getString("salt");

                // Validate the entered password using the stored salt
                String enteredPasswordHash = hashPassword(password, salt);

                return hashedPassword.equals(enteredPasswordHash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /*private void togglePasswordVisibility() {
        if (showPasswordToggle.isSelected()) {
            passwordTextField.setPromptText(passwordTextField.getText());
            passwordTextField.setPromptText("");
        } else {
            passwordTextField.setText(passwordTextField.getPromptText());
            passwordTextField.setText("");
        }
    }*/


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطأ");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String hashPassword(String password, String salt) {
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
