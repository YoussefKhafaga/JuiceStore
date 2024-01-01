package com.example.store.GUI.Purchases;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.scene.control.Alert;
import org.bson.Document;

public class AddPurchases {
    public void AddPurchase(Purchases purchase) {
        // Connect to MongoDB server
        try (
                MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> purchaseCollection = database.getCollection("Purchases");


            // Check if an admin with the same username already exists
            Document purchaseDocuemnt = purchaseCollection.find(Filters.eq("name", purchase.getPurchaseName())).first();

            if (purchaseDocuemnt != null) {
            } else {
                // Insert the document into the collection
                Document newPurchaseDocument = new Document()
                        .append("name", purchase.getPurchaseName())
                        .append("price", purchase.getPurchasePrice())
                        .append("date", purchase.getDate())
                        .append("time", purchase.getTime());

                purchaseCollection.insertOne(newPurchaseDocument);

                createAlertBox("تم التسجيل بنجاح", "نجاح التسجيل", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createAlertBox(String msg, String type, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}