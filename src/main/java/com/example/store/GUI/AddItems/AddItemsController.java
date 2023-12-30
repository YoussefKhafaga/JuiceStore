package com.example.store.GUI.AddItems;

import com.example.store.GUI.Categories.AddCategories;
import com.example.store.Product.AddProductDocument;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.List;

public class AddItemsController {
    @FXML
    private AnchorPane addItemsAnchorPane;
    @FXML
    private TextField itemName;
    @FXML
    private TextField itemPrice;
    @FXML
    private TextArea itemDescription;
    @FXML
    private Button addItem;
    @FXML
    private Button backButton;
    @FXML
    private Button editProductButton;
    @FXML
    private ComboBox<String> itemCategory;

    public void initialize(){
        AddCategories addCategories = new AddCategories();
        List<String> categories;
        categories = addCategories.getAllCategories();
        populateCategories(categories);

        addItem.setOnAction(actionEvent -> {
            handleSubmitButton();
        });

        backButton.setOnAction(actionEvent -> {
            handleBackButton();
        });

        editProductButton.setOnAction(actionEvent -> {
            handleEditButton();
        });
    }

    public void handleSubmitButton()
    {

        if (itemName.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("تحذير");
            alert.setHeaderText(null);
            alert.setContentText("من فضلك ادخل اسم المنتج");

            alert.showAndWait();
        }
        else if (itemPrice.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("تحذير");
            alert.setHeaderText(null);
            alert.setContentText("من فضلك ادخل سعر المنتج");

            alert.showAndWait();
        }
        else if (itemCategory.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("تحذير");
            alert.setHeaderText(null);
            alert.setContentText("من فضلك اختر نوع المنتج");

            alert.showAndWait();
        }
        else {
            Document document = new Document()
                    .append("name", itemName.getText())
                    .append("price", Double.parseDouble(itemPrice.getText()))
                    .append("category", itemCategory.getValue())
                    .append("description", itemDescription.getText());
            AddProductDocument addProductDocument = new AddProductDocument(document);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("عملية ناجحة");
            alert.setHeaderText(null);
            alert.setContentText("تم ادخال المنتج بنجاح");

            alert.showAndWait();
        }

    }
    public void handleBackButton(){
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Menu/Menu.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage currentStage = (Stage) addItemsAnchorPane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Menu");
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void handleEditButton()
    {
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/AddItems/edit-items.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage currentStage = (Stage) addItemsAnchorPane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("edit items");
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    private void populateCategories(List<String> categories) {
        // Clear existing items and add new ones
        itemCategory.getItems().clear();
        itemCategory.getItems().addAll(categories);
    }

}