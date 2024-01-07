package com.example.store.GUI.Login;

import com.example.store.Admin.AddAdminDocument;
import com.example.store.Workers;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.bson.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Base64;
import java.util.Optional;

public class HelloController {

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Button startButton;
    @FXML
    public Button addAdminButton;
    @FXML
    private Button AddWorker;
    @FXML
    private Button Returns;

    // MongoDB connection details
    private static final String MONGO_DB_URL = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "KhanMariaStore";
    private static final String COLLECTION_NAME = "Admins";

    public void initialize() {
        startButton.setOnAction(event -> switchToSecondView());
        //LoginButton.setOnAction(actionEvent -> handleLogin());
        //showPasswordToggle.setOnAction(event -> togglePasswordVisibility());
        addAdminButton.setOnAction(actionEvent -> {
            addAdmin();
        });
        AddWorker.setOnAction(actionEvent -> {
            handleAddWorker();
        });
        Returns.setOnAction(actionEvent -> {
            handleReturnsView();
        });
    }

    public void handleReturnsView() {
        // Create a TextInputDialog with both username and password fields
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Authentication");
        dialog.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("اسم المستخدم");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("كلمة المرور");

        grid.add(new Label("اسم المستخدم:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("كلمة المرور:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            String username = usernamePassword.getKey();
            String password = usernamePassword.getValue();

            HelloController helloController = new HelloController();

            // Validate the credentials (replace this with your authentication logic)
            if (helloController.validateLogin(username, password)) {
                // Load the Returns view
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Returns/returns.fxml"));
                    Scene scene = new Scene(loader.load());
                    scene.getStylesheets().add("/styles.css");

                    Stage currentStage = (Stage) anchorpane.getScene().getWindow();
                    currentStage.setScene(scene);
                    currentStage.setTitle("المرتجعات");
                    currentStage.setResizable(false);
                    currentStage.centerOnScreen();
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            } else {
                // Incorrect credentials, show an alert or take appropriate action
                Alert alert = new Alert(Alert.AlertType.ERROR, "خطأ في اسم المستخدم وكلمة المرور", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }
    public void handleAddWorker(){
        // Create a TextInputDialog with both username and password fields
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("تسجيل دخول الادمن");
        dialog.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور للادمن");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("اسم المستخدم");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("كلمة المرور");

        grid.add(new Label("اسم المستخدم:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("كلمة المرور:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            String username = usernamePassword.getKey();
            String password = usernamePassword.getValue();

            HelloController helloController = new HelloController();

            // Validate the credentials (replace this with your authentication logic)
            if (helloController.validateLogin(username, password)) {
                // Create a TextInputDialog with both username and password fields
                Dialog<Pair<String, String>> dialog1 = new Dialog<>();
                dialog1.setTitle("تسجيل موظف جديد");
                dialog1.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور");

                // Set the button types
                ButtonType loginButtonType1 = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
                dialog1.getDialogPane().getButtonTypes().addAll(loginButtonType1, ButtonType.CANCEL);

                // Create the username and password labels and fields
                GridPane grid1 = new GridPane();
                grid1.setHgap(10);
                grid1.setVgap(10);
                grid1.setPadding(new Insets(20, 150, 10, 10));

                TextField usernameField1 = new TextField();
                usernameField1.setPromptText("اسم المستخدم");
                PasswordField passwordField1 = new PasswordField();
                passwordField1.setPromptText("كلمة المرور");

                grid1.add(new Label("اسم المستخدم:"), 0, 0);
                grid1.add(usernameField1, 1, 0);
                grid1.add(new Label("كلمة المرور:"), 0, 1);
                grid1.add(passwordField1, 1, 1);

                // Enable/Disable login button depending on whether a username was entered
                Node loginButton1 = dialog1.getDialogPane().lookupButton(loginButtonType1);
                loginButton1.setDisable(true);

                // Do some validation
                usernameField1.textProperty().addListener((observable, oldValue, newValue) -> {
                    loginButton1.setDisable(newValue.trim().isEmpty());
                });

                dialog1.getDialogPane().setContent(grid1);

                // Convert the result to a username-password-pair when the login button is clicked
                dialog1.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType1) {
                        return new Pair<>(usernameField1.getText(), passwordField1.getText());
                    }
                    return null;
                });

                // Show the dialog and wait for the user's response
                Optional<Pair<String, String>> result1 = dialog1.showAndWait();

                result1.ifPresent(usernamePassword1 -> {
                    String username1 = usernamePassword1.getKey();
                    String password1 = usernamePassword1.getValue();

                    //add worker
                    Workers workers = new Workers(username1, password1);
                    if (workers.addWorker(username1, password1)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "تم تسجيل الموظف بنجاح", ButtonType.OK);
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "لم يتم تسجيل الموظف بنجاح", ButtonType.OK);
                        alert.showAndWait();
                    }
                });

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "خطأ في اسم المستخدم وكلمة مرور الادمن", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }


    private void addAdmin() {
        // Create a TextInputDialog with both username and password fields
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("تسجيل دخول الادمن");
        dialog.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور للادمن");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("اسم المستخدم");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("كلمة المرور");

        grid.add(new Label("اسم المستخدم:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("كلمة المرور:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            String username = usernamePassword.getKey();
            String password = usernamePassword.getValue();

            HelloController helloController = new HelloController();

            // Validate the credentials (replace this with your authentication logic)
            if (helloController.validateLogin(username, password)) {
                // Create a TextInputDialog with both username and password fields
                Dialog<Pair<String, String>> dialog1 = new Dialog<>();
                dialog1.setTitle("تسجيل ادمن");
                dialog1.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور للادمن الجديد");

                // Set the button types
                ButtonType loginButtonType1 = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
                dialog1.getDialogPane().getButtonTypes().addAll(loginButtonType1, ButtonType.CANCEL);

                // Create the username and password labels and fields
                GridPane grid1 = new GridPane();
                grid1.setHgap(10);
                grid1.setVgap(10);
                grid1.setPadding(new Insets(20, 150, 10, 10));

                TextField usernameField1 = new TextField();
                usernameField1.setPromptText("اسم المستخدم");
                PasswordField passwordField1 = new PasswordField();
                passwordField1.setPromptText("كلمة المرور");

                grid1.add(new Label("اسم المستخدم:"), 0, 0);
                grid1.add(usernameField1, 1, 0);
                grid1.add(new Label("كلمة المرور:"), 0, 1);
                grid1.add(passwordField1, 1, 1);

                // Enable/Disable login button depending on whether a username was entered
                Node loginButton1 = dialog1.getDialogPane().lookupButton(loginButtonType1);
                loginButton1.setDisable(true);

                // Do some validation
                usernameField1.textProperty().addListener((observable, oldValue, newValue) -> {
                    loginButton1.setDisable(newValue.trim().isEmpty());
                });

                dialog1.getDialogPane().setContent(grid1);

                // Convert the result to a username-password-pair when the login button is clicked
                dialog1.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType1) {
                        return new Pair<>(usernameField1.getText(), passwordField1.getText());
                    }
                    return null;
                });

                // Show the dialog and wait for the user's response
                Optional<Pair<String, String>> result1 = dialog1.showAndWait();

                result1.ifPresent(usernamePassword1 -> {
                    String username1 = usernamePassword1.getKey();
                    String password1 = usernamePassword1.getValue();
                    AddAdminDocument adminDocument = new AddAdminDocument();
                    adminDocument.addAdmin(username1, password1);
                });
            }
        });
    }

    private void switchToSecondView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Menu/Menu.fxml"));
            Scene scene = new Scene(loader.load());

            Stage currentStage = (Stage) anchorpane.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Menu");
            currentStage.setResizable(false);
            //currentStage.setMaximized(true);
            currentStage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void handleLogin() {
        String enteredUsername = userNameTextField.getText();
        String enteredPassword = passwordTextField.getText();

        // Connect to MongoDB and validate login
        if (validateLogin(enteredUsername, enteredPassword)) {
            switchToSecondView();
        } else {
            showErrorAlert("اسم المستخدم او كلمة المرور غير صحيحة");
        }
    }*/

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
