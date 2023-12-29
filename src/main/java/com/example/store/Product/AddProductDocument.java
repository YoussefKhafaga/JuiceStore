package com.example.store.Product;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
                    System.out.println("Document added successfully!");
                } else {
                    // Document already exists, handle accordingly
                    System.out.println("Document already exists!");
                    // You may choose to update the existing document or take other actions
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
