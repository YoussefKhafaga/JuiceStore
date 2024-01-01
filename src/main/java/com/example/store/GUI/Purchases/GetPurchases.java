package com.example.store.GUI.Purchases;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.internal.connection.Time;
import javafx.scene.control.Alert;
import org.bson.Document;

import java.util.Date;
import java.util.List;

public class GetPurchases {
    /*public List<Document> getAllPurchases(Date date, Time time)
    {
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
                return purchaseDocuemnt;
            } else {
                createAlertBox("لا يوجد مشتري بهذا الاسم والسعر", "عملية فاشلة", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    public Document getPurchase(Purchases purchase) {
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
                return purchaseDocuemnt;
            } else {
                createAlertBox("لا يوجد مشتري بهذا الاسم والسعر", "عملية فاشلة", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void createAlertBox(String msg, String type, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
