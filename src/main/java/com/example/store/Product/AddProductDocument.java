package com.example.store.Product;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.Alert;
import org.bson.Document;

public class AddProductDocument {
    public AddProductDocument(Document document) {
            // Connect to MongoDB server
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

                // Specify the database
                MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

                // Specify the collection
                MongoCollection<Document> adminsCollection = database.getCollection("Products");

                Document existingDocument = adminsCollection.find(document).first();

                if (existingDocument == null) {
                    // Insert the document into the collection
                    adminsCollection.insertOne(document);
                    createAlertBox("تم اضافة المنتج بنجاح", "اضافة منتج جديد", Alert.AlertType.INFORMATION);
                } else {
                    // Document already exists, handle accordingly
                    createAlertBox("المنتج موجود بالفعل", "اضافة منتج جديد", Alert.AlertType.INFORMATION);
                    // You may choose to update the existing document or take other actions
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public void createAlertBox(String msg, String type, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    }
