package com.example.store.GUI.AddItems;

import com.example.store.GUI.Categories.AddCategories;
import com.example.store.Product.GetProductDocument;
import com.example.store.Product.Products;
import com.mongodb.client.MongoClients;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private ComboBox categoryCombobox;
    @FXML
    private ComboBox productsCombobox;
    @FXML
    private ComboBox newCombobox;

    @FXML
    public void initialize() {
        AddCategories addCategories = new AddCategories();
        List<String> categories = addCategories.getAllCategories();
        populateCategories(categories);

        categoryCombobox.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            if (newValue instanceof String) {
                String selectedCategory = (String) newValue;
                GetProductDocument getProductDocument = new GetProductDocument();
                List<Products> productsList = getProductDocument.retrieveProductsByCategory(selectedCategory);

                // Extract product names from the retrieved products
                List<String> productNames = productsList.stream().map(Products::getProductName).collect(Collectors.toList());

                // Update the product ComboBox with the new product names
                populateProducts(productNames);
            }
        });

        editItemBackButton.setOnAction(actionEvent -> {
            handleEditItemBack();
        });

        editItem.setOnAction(actionEvent -> {
            handleEditItem();
        });
    }

    @FXML
    private void handleEditItem() {
        // Assuming you have a method to get the currently selected product
        Products selectedProduct = getSelectedProduct();
        // Check if the new values are not empty and update the product accordingly

        // Assuming you have a method to update the product in the database
        updateProduct(selectedProduct);
    }

    // Method to get the currently selected product (replace with your logic)
    private Products getSelectedProduct() {
        // Retrieve the selected product from the database or wherever it is stored
        String productName = productsCombobox.getValue().toString();
        return new Products(productName);
    }

    // Method to update the product in the database (replace with your logic)
    private void updateProduct(Products product) {
        // Get the new values from the input fields and combobox
        String newName = editItemName.getText();
        String newPrice = editItemPrice.getText();
        String newCategory = newCombobox.getValue() != null ? newCombobox.getValue().toString() : null;
        // Connect to the MongoDB server and update the product in the "Products" collection
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var productsCollection = database.getCollection("Products");

            // Create a filter to find the document with the specified name
            var filter = eq("name", product.getProductName());
            var productDocument = productsCollection.find(filter).first();
            System.out.println(productDocument);
            Products updatedProduct = new Products();
            if (productDocument != null) {
                // Extract product information from the document
                String productName = productDocument.getString("name");
                Double productPrice = productDocument.getDouble("price");
                String productCategory = productDocument.getString("category");
                String productDescription = productDocument.getString("description");
                if (!Objects.equals(newName, "")) {
                    updatedProduct.setProductName(newName.trim());
                } else {
                    updatedProduct.setProductName(productName);
                }

                if (!Objects.equals(newPrice, "")) {
                    updatedProduct.setProductPrice(Double.parseDouble(newPrice.trim()));
                } else {
                    updatedProduct.setProductPrice(productPrice);
                }

                if (newCategory != null && !newCategory.isEmpty()) {
                    updatedProduct.setCategory(newCategory);
                } else {
                    updatedProduct.setCategory(productCategory);
                }
                updatedProduct.setDescription(productDescription);

            }
            // Create a document with the updated values
            var updateDocument = new Document("$set", new Document()
                    .append("name", updatedProduct.getProductName())
                    .append("price", updatedProduct.getProductPrice())
                    .append("category", updatedProduct.getCategory())
                    .append("description", updatedProduct.getDescription()));

            // Update the document in the collection
            productsCollection.updateOne(filter, updateDocument);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("عملية ناجحة");
            alert.setHeaderText(null);
            alert.setContentText("تم تعديل المنتج بنجاح");

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
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
            currentStage.setResizable(false);
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private void populateCategories(List<String> categories) {
        // Clear existing items and add new ones
        categoryCombobox.getItems().clear();
        categoryCombobox.getItems().addAll(categories);
        newCombobox.getItems().clear();
        newCombobox.getItems().addAll(categories);
    }
    private void populateProducts(List<String> products)
    {
        // Clear existing items and add new ones
        productsCombobox.getItems().clear();
        productsCombobox.getItems().addAll(products);
    }

    /*public Products searchProductByName(String name) {
        // Connect to the database and search for a product with a matching name
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("YourDatabaseName");
            var productsCollection = database.getCollection("Products");

            var searchQuery = eq("name", name);
            var searchResultsCursor = productsCollection.find(searchQuery);

            if (searchResultsCursor.iterator().hasNext()) {
                // Only return the first matching product
                Document document = searchResultsCursor.first();
                return new Products(
                        document.getString("name"),
                        document.getDouble("price"),
                        document.getString("description"),
                        document.getString("category")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return null if no matching product is found
        return null;
    }
*/
}
