package com.example.store.GUI.Menu;

import com.example.store.GUI.Login.HelloController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class MenuController {
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Button logOut;
    @FXML
    private Button addProducts;
    @FXML
    private Button cashier;
    @FXML
    private Button reports;
    @FXML
    private Button CategoriesButton;

    public void initialize(){
        updateDateLabel(getCurrentDate());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateClock(timeLabel))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        //logout button
        logOut.setOnAction(event -> switchToLogInView());

        addProducts.setOnAction(event -> switchToAddProductsView());

        cashier.setOnAction(event -> switchToCashierView());

        reports.setOnAction(event -> switchToReportsView());

        CategoriesButton.setOnAction(actionEvent -> {switchToCategoriesView();});

    }

    private String getCurrentDate() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Format the date as a string
        return currentDate.toString();
    }

    public Label getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(Label dateLabel) {
        this.dateLabel = dateLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    public void updateDateLabel(String text) {
        dateLabel.setText(text);
    }
    private void updateClock(Label clockText) {
        // Get the current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String currentTime = dateFormat.format(new Date());
        // Update the text of the clock
        clockText.setText(currentTime);
    }
    public void switchToLogInView() {
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Login/hello-view.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) anchorpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("LogIn");
            currentStage.setResizable(false);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    public void switchToAddProductsView(){
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/AddItems/add-items.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) anchorpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Add Products");
            currentStage.setResizable(false);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    public void switchToCashierView() {
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Cashier/cashier.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) anchorpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Cashier");
            currentStage.setMaximized(true);
            currentStage.setResizable(true);
            currentStage.centerOnScreen();


        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    /*public void switchToReportsView(){
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Reports/reports.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) anchorpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Reports");
            currentStage.setResizable(false);
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }*/
    public void switchToMenuView()
    {
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Menu/Menu.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) anchorpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Menu");
            currentStage.setResizable(false);
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    public void switchToCategoriesView()
    {
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Categories/Categories.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) anchorpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Categories");
            currentStage.setResizable(false);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    public void switchToReportsView() {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Reports/reports.fxml"));
                    Scene scene = new Scene(loader.load());
                    scene.getStylesheets().add("/styles.css");

                    Stage currentStage = (Stage) anchorpane.getScene().getWindow();
                    currentStage.setScene(scene);
                    currentStage.setTitle("التقارير");
                    currentStage.setResizable(true);
                    currentStage.setMaximized(true);
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
}
