package com.example.store.GUI.Categories;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddCategories {
        @FXML
        private TextField categoryNameTextField;
        @FXML
        private Button AddButton;
        @FXML
        private Button BackButton;
        @FXML
        AnchorPane anchorpane;
        @FXML
        ComboBox<String> comboBox;
        @FXML
        TextField categoryNameTextFieldeditordelete;
        @FXML
        Button editButton;
        @FXML
        Button deleteButton;
        public void initialize()
        {
            populateCategories();
            AddButton.setOnAction(actionEvent -> {
                if (categoryNameTextField.getText() != "" || categoryNameTextField.getText() != null) {
                    addCategory(categoryNameTextField.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("عملية ناجحة");
                    alert.setHeaderText(null);
                    alert.setContentText("تم ادخال النوع بنجاح");

                    alert.showAndWait();
                }
            });

            BackButton.setOnAction(actionEvent -> {
                switchToMenuView();
            });
            editButton.setOnAction(actionEvent -> {
                editCategory();
            });
            deleteButton.setOnAction(actionEvent -> {
                deleteCategory();
            });
        }

    public void switchToMenuView(){
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
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    public void addCategory(String categoryName) {
        // Connect to MongoDB server
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> adminsCollection = database.getCollection("Categories");

            // Create a new document
            Document newCategoryDocument = new Document()
                    .append("categoryname", categoryName);
            // Insert the document into the collection
            adminsCollection.insertOne(newCategoryDocument);
            populateCategories();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> categoriesCollection = database.getCollection("Categories");

            // Find all documents in the collection
            FindIterable<Document> documents = categoriesCollection.find();

            // Iterate over the documents and extract category names
            for (Document document : documents) {
                String categoryName = document.getString("categoryname");
                categories.add(categoryName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }
    private void populateCategories() {
        // Assuming you have a method to retrieve categories from the database
        List<String> categories = getAllCategories();

        // Clear existing items and add new ones
        comboBox.getItems().clear();
        comboBox.getItems().addAll(categories);
    }
    public void editCategory() {
        if (categoryNameTextFieldeditordelete.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("تحذير");
            alert.setHeaderText(null);
            alert.setContentText("من فضلك ادخل اسم النوع");
            alert.showAndWait();
        } else if (comboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("تحذير");
            alert.setHeaderText(null);
            alert.setContentText("من فضلك اختر نوع المنتج");
            alert.showAndWait();
        } else {
            // Connect to MongoDB server
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                // Specify the database
                MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

                // Specify the collection
                MongoCollection<Document> categoriesCollection = database.getCollection("Categories");

                // Define the filter to find the existing category
                Document filter = new Document("categoryname", comboBox.getValue().toString());

                // Find the existing document
                Document existingDocument = categoriesCollection.find(filter).first();

                if (existingDocument == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("تحذير");
                    alert.setHeaderText(null);
                    alert.setContentText("النوع غير موجود");
                    alert.showAndWait();
                } else {
                    // Get the new category name from the TextField
                    String newCategoryName = categoryNameTextFieldeditordelete.getText();

                    // Update the document with the new category name
                    Document update = new Document("$set", new Document("categoryname", newCategoryName));

                    // Perform the update
                    categoriesCollection.updateOne(filter, update);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("عملية ناجحة");
                    alert.setHeaderText(null);
                    alert.setContentText("تم تعديل النوع بنجاح");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        populateCategories();
    }

    public void deleteCategory(){
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> categoriesCollection = database.getCollection("Categories");

            // Delete the document with the specified category name
            categoriesCollection.deleteOne(Filters.eq("categoryname", comboBox.getValue()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("عملية ناجحة");
            alert.setHeaderText(null);
            alert.setContentText("تم لحذف بنجاح");

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        populateCategories();
    }
}
