package com.example.store.Sales;

import com.example.store.Product.Products;
import com.example.store.Sales.Sales;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

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
            sale.setSaleDate(LocalDate.now());
            // Check if a sale with the same id already exists
            Document existingSale = salesDocument.find(and(
                            eq("saleDate", LocalDate.now()),
                            eq("id", sale.getId())))  // Replace yourSaleId with the specific sale ID you're looking for
                    .sort(descending("id"))
                    .limit(1)
                    .first();


            if (existingSale != null) {
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
