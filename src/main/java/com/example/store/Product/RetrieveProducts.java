package com.example.store.Product;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class RetrieveProducts {
    public Products retrieveProductByName(String targetName) {
        // Connect to the MongoDB server (adjust connection string accordingly)
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Connect to the "your_database_name" database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Connect to the "products" collection
            MongoCollection<Document> collection = database.getCollection("Products");

            // Define a filter to get documents with a specific name
            Document filter = new Document("name", targetName);

            // Retrieve a single document that matches the filter
            Document productDocument = collection.find(filter).first();

            if (productDocument != null) {
                // Extract product information from the document
                String name = productDocument.getString("name");
                Double price = productDocument.getDouble("price");
                String category = productDocument.getString("category");
                String description = productDocument.getString("description");

                return new Products(name, price, category, description);
            }
        }

        return null; // Return null if no product with the specified name is found
    }


    public List<String> retrieveCategories() {
        List<String> categories = new ArrayList<>();

        // Connect to the MongoDB server (adjust connection string accordingly)
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Connect to the "your_database_name" database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Connect to the "products" collection
            MongoCollection<Document> collection = database.getCollection("Products");

            // Use the distinct operation to get unique categories
            List<String> distinctCategories = collection.distinct("category", String.class).into(new ArrayList<>());

            categories.addAll(distinctCategories);
        }

        return categories;
    }
    public List<Products> retrieveProductsByCategory(String targetCategory) {
        List<Products> productsList = new ArrayList<>();

        // Connect to the MongoDB server (adjust connection string accordingly)
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Connect to the "your_database_name" database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Connect to the "products" collection
            MongoCollection<Document> collection = database.getCollection("Products");

            // Define a filter to get documents with a specific category
            Document filter = new Document("category", targetCategory);

            // Retrieve documents that match the filter
            MongoCursor<Document> cursor = collection.find(filter).iterator();
            try {
                while (cursor.hasNext()) {
                    Document productDocument = cursor.next();

                    // Extract product information from the document
                    String name = productDocument.getString("name");
                    Double price = productDocument.getDouble("price");
                    String category = productDocument.getString("category");
                    String description = productDocument.getString("description");

                    Products products = new Products(name, price, category, description);
                    // Create a Product object or use the retrieved data as needed
                    productsList.add(products);
                }
            } finally {
                cursor.close();
            }
        }

        return productsList;
    }
}
