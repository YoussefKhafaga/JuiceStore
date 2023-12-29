package com.example.store.Sales;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class AddSalesDocument {
    public static void main(String[] args) {
        // Connect to MongoDB server
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> adminsCollection = database.getCollection("Sales");

            // Create a new document
            Document newAdminDocument = new Document()
                    .append("pr", "YoussefKhafaga")
                    .append("password", "secretpassword");

            // Insert the document into the collection
            adminsCollection.insertOne(newAdminDocument);

            System.out.println("Document added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
