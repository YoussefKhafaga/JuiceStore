package com.example.store.GUI.AddItems;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class editProductController {

    @FXML
    private AnchorPane editItemsAnchorPane;
    @FXML
    private TextField editItemName;

    @FXML
    private TextField editItemPrice;

    @FXML
    private TextArea editItemDescription;

    @FXML
    private Button editItemBackButton;

    @FXML
    private Button editItem;

    @FXML
    private TextField searchBar;

    @FXML
    public void initialize() {
        // Add a listener to the search bar text property to respond to changes
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call a method to handle the search with the new text
            handleSearch(newValue);
        });

        editItemBackButton.setOnAction(actionEvent -> {
            handleEditItemBack();
        });
    }

    @FXML
    private void handleEditItem() {
        // Handle the logic for editing the product
        String newName = editItemName.getText();
        String newPrice = editItemPrice.getText();
        String newDescription = editItemDescription.getText();

        // Perform the update in the database or wherever you store your product data
        // ...

        // Optionally, you can navigate back to the previous view
        // using the editItemBackButton or perform other actions.
    }

    @FXML
    private void handleEditItemBack() {
        // Handle the logic for navigating back to the previous view
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/AddItems/add-items.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage currentStage = (Stage) editItemsAnchorPane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("اضافة المنتج");
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private void handleSearch(String searchText) {
        // Implement your search logic here
        // You might want to interact with your database and update the UI accordingly
        System.out.println("Searching for: " + searchText);
    }
}
