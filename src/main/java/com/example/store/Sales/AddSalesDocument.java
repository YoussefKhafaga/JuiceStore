package com.example.store.Sales;

import com.example.store.Product.Products;
import com.example.store.Sales.Sales;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.stream.Collectors;

public class AddSalesDocument {
    public void AddSale(Sales sale) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> salesDocument = database.getCollection("Sales");

            if (salesDocument == null) {
                System.err.println("Error: Sales collection is null.");
                return;
            }

            // Check if a sale with the same id already exists
            Document existingSale = salesDocument.find(Filters.eq("id", sale.getId())).first();

            if (existingSale != null) {
                // Sale with the same ID already exists, handle it if needed
            } else {
                // Insert the document into the collection
                Document newSaleDocument = new Document()
                        .append("id", sale.getId())
                        .append("products", sale.getProducts().stream().map(Products::toDocument).collect(Collectors.toList()))
                        .append("totalPrice", sale.getTotalPrice())
                        .append("totalPaid", sale.getPaid())
                        .append("remaining", sale.getRemaining())
                        .append("saleDate", sale.getSaleDate())
                        .append("saleTime", sale.getSaleTime());

                salesDocument.insertOne(newSaleDocument);

                System.out.println("Sale document added successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
