package com.example.store.GUI.Purchases;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class PurchasesController {
    @FXML
    private DatePicker purchaseDatePicker;
    @FXML
    private TextField purchaseNameTextField;
    @FXML
    private TextField purchasePriceTextField;
    @FXML
    private Button AddPurchaseButton;
    @FXML
    private Button BackButton;
    @FXML
    private AnchorPane anchorpane;

    public void initialize() {
        AddPurchaseButton.setOnAction(actionEvent -> handleAddPurchase());
        BackButton.setOnAction(actionEvent -> {
            switchToCashierVeiw();
        });
    }

    private void handleAddPurchase() {
        // Retrieve values from the controls
        LocalDate purchaseDate = purchaseDatePicker.getValue();
        String purchaseName = purchaseNameTextField.getText().trim();
        String purchasePriceStr = purchasePriceTextField.getText().trim();

        // Check if any information is missing
        if (purchaseDate == null || purchaseName.isEmpty() || purchasePriceStr.isEmpty()) {
            // Show an alert for missing information
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("معلومة مفقودة");
            alert.setHeaderText(null);
            alert.setContentText("من فضلك ادخل كل المعلومات");
            alert.showAndWait();
        } else {
            try {
                double purchasePrice = Double.parseDouble(purchasePriceStr);

                // Now you can use the purchaseDate, purchaseName, and purchasePrice to add the purchase
                // Add your logic here

                // Optionally, clear the fields after adding the purchase
                Purchases purchases = new Purchases(purchaseName, purchasePrice, purchaseDate);
                AddPurchases addPurchases = new AddPurchases();
                addPurchases.AddPurchase(purchases);
                clearFields();
            } catch (NumberFormatException e) {
                // Handle the case where the purchase price is not a valid double
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ في السعر");
                alert.setHeaderText(null);
                alert.setContentText("من فضلك ادخل السعر بشكل صحيح");
                alert.showAndWait();
            }
        }
    }


    private void clearFields() {
        // Clear the input fields
        purchaseDatePicker.setValue(null);
        purchaseNameTextField.clear();
        purchasePriceTextField.clear();
    }

    public void switchToCashierVeiw()
    {
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
            currentStage.setResizable(true);
            currentStage.setMaximized(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
