package com.example.store.GUI.Returns;

import com.mongodb.client.MongoClients;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.mongodb.client.model.Filters.*;

public class ReturnsController {
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private DatePicker DateSelector;
    @FXML
    private TextField saleId;
    @FXML
    private Button DeleteSaleButton;
    @FXML
    private ScrollPane scrollablePane;
    @FXML
    private Button BackButton;
    Document saleDocument;
    @FXML
    private Button enterBoth;

    private boolean bothValuesEntered = false;

    public void initialize() {
        enterBoth.setOnAction(actionEvent -> {
            checkBothValuesEntered();
            if (bothValuesEntered) {
                fetchAndDisplaySale();
            }
            bothValuesEntered = false;
        });
        BackButton.setOnAction(actionEvent -> {
            switchToHelloView();
        });
        // Set up action for DeleteSaleButton click
        DeleteSaleButton.setOnAction(event -> onDeleteSaleButtonClick());
        // Add listener to DateSelector
        /*DateSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Check if both DateSelector and saleId have values
            checkBothValuesEntered();
            // If both values are entered, fetch and display the sale

        });

        // Add listener to saleId
        saleId.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if both DateSelector and saleId have values
            checkBothValuesEntered();
            // If both values are entered, fetch and display the sale
        });*/
    }

    public void switchToHelloView()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Login/hello-view.fxml"));
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
    private void onDeleteSaleButtonClick() {
        if (saleDocument != null) {
            // Implement your delete logic here
            MongoClients.create("mongodb://localhost:27017")
                    .getDatabase("KhanMariaStore")
                    .getCollection("Sales").deleteOne(saleDocument);
            createAlertBox("تم حذف العملية بنجاح", "نجاح العملية", Alert.AlertType.CONFIRMATION);
        } else {
            // Handle the case where saleDocument is null, perhaps show an error message
            createAlertBox("فشل الحذف", "فشل العملية", Alert.AlertType.CONFIRMATION);
        }
    }

    private void checkBothValuesEntered() {
        LocalDate selectedDate = DateSelector.getValue();
        String saleIdText = saleId.getText();

        // Check if both DateSelector and saleId have values
        bothValuesEntered = (selectedDate != null && !saleIdText.isEmpty());
    }

    public void fetchAndDisplaySale() {
        // Get values from DateSelector and saleId
        LocalDate selectedDate = DateSelector.getValue();
        String saleIdText = saleId.getText();

        // Check if both DateSelector and saleId have values
        if (selectedDate != null && !saleIdText.isEmpty()) {
            // Convert LocalDate to MongoDB date format
            String formattedDate = selectedDate.toString();

            // Query MongoDB for the sale based on date and saleId
            saleDocument = MongoClients.create("mongodb://localhost:27017")
                    .getDatabase("KhanMariaStore")
                    .getCollection("Sales")
                    .find(and(
                            eq("saleDate", selectedDate),
                            eq("id", Integer.parseInt(saleIdText))))
                    .first();

            // If the sale is found, display it in the scrollablePane
            if (saleDocument != null) {
                displaySaleInScrollPane(saleDocument);
                // Set the class variable if needed: saleDocument = fetchedSaleDocument;
                // TODO: Implement logic to delete the sale from the database
                // salesCollection.deleteOne(fetchedSaleDocument);
            } else {
                // Sale not found, handle as needed (e.g., show an error message)
                createAlertBox("لم يتم العثور علي العملية", "خطأ", Alert.AlertType.ERROR);
            }
        } else {
            // DateSelector or saleId is missing, handle as needed (e.g., show an error message)
            createAlertBox("من فضلك اختار التاريخ والرقم", "خطأ", Alert.AlertType.ERROR);
        }
    }

    private void displaySaleInScrollPane(Document saleDocument) {
        Platform.runLater(() -> {
            VBox saleDetails = createSaleDetailsUI(saleDocument);
            scrollablePane.setContent(saleDetails);
        });
    }

    private VBox createSaleDetailsUI(Document saleDocument) {
        VBox saleDetailsVBox = new VBox();
        saleDetailsVBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);

        // Set the font size for the VBox
        saleDetailsVBox.setStyle("-fx-font-size: 18;");
        // Display sale ID
        Label saleIdLabel = new Label("رقم العملية: " + saleDocument.getInteger("id"));
        saleIdLabel.setWrapText(true); // Enable text wrapping
        saleDetailsVBox.getChildren().add(saleIdLabel);
        saleDetailsVBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);

        // Display sale date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ar"));
        String formattedDate = saleDocument.getDate("saleDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter);
        Label saleDateLabel = new Label("تاريخ العملية: " + formattedDate);
        saleDateLabel.setWrapText(true); // Enable text wrapping
        saleDetailsVBox.getChildren().add(saleDateLabel);

        // Display products
        List<Document> products = (List<Document>) saleDocument.get("products");
        for (Document product : products) {
            Label productNameLabel = new Label("اسم المنتج: " + product.getString("productName"));
            Label productPriceLabel = new Label("سعر المنتج: " + product.getDouble("productPrice"));
            Label quantityLabel = new Label("الكمية: " + product.getDouble("quantity"));

            // Enable text wrapping for each label
            productNameLabel.setWrapText(true);
            productPriceLabel.setWrapText(true);
            quantityLabel.setWrapText(true);

            // Add product details to VBox
            saleDetailsVBox.getChildren().addAll(productNameLabel, productPriceLabel, quantityLabel);

            // Add a separator between products
            saleDetailsVBox.getChildren().add(new javafx.scene.control.Separator());
        }

        // Display total price, total paid, remaining, etc.
        Label totalPriceLabel = new Label("اجمالي السعر: " + saleDocument.getDouble("totalPrice"));
        Label totalPaidLabel = new Label("المدفوع: " + saleDocument.getDouble("totalPaid"));
        Label remainingLabel = new Label("الباقي: " + saleDocument.getDouble("remaining"));

        // Enable text wrapping for each label
        totalPriceLabel.setWrapText(true);
        totalPaidLabel.setWrapText(true);
        remainingLabel.setWrapText(true);

        // Add remaining details to VBox
        saleDetailsVBox.getChildren().addAll(totalPriceLabel, totalPaidLabel, remainingLabel);

        return saleDetailsVBox;
    }

    public void createAlertBox(String msg, String type, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void switchToCashierView()
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
