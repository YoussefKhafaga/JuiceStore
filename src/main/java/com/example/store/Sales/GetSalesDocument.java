package com.example.store.Sales;

import com.example.store.Product.Products;
import com.mongodb.client.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class GetSalesDocument {
    public TotalSummary getTotalSummary(LocalDate startDate, LocalDate endDate) {
        TotalSummary totalSummary = new TotalSummary();

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var salesCollection = database.getCollection("Sales");

            // Match documents within the date range
            Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            // Match documents within the date range
            var matchPipeline = Arrays.asList(
                    match(and(
                            gte("saleDate", start),
                            lt("saleDate", end)
                    )),
                    unwind("$products"),  // Unwind the products array
                    project(fields(
                            include("totalPaid", "remaining", "totalPrice", "products.quantity"),
                            computed("formattedDate", new Document("$dateToString",
                                    new Document("format", "%Y-%m-%d").append("date", "$saleDate")))
                    )),
                    match(and(
                            gte("formattedDate", startDate.toString()),
                            lt("formattedDate", endDate.plusDays(1).toString())
                    ))
            );

            var groupPipeline = Arrays.asList(
                    group(null,
                            sum("totalPaid", "$totalPaid"),
                            sum("remaining", "$remaining"),
                            sum("totalPrice", "$totalPrice"),
                            sum("totalQuantity", "$products.quantity")  // Sum the quantity
                    )
            );

// Combine the two lists
            List<Bson> aggregationPipeline = new ArrayList<>();
            aggregationPipeline.addAll(matchPipeline);
            aggregationPipeline.addAll(groupPipeline);

// Execute the aggregation pipeline
            List<Document> aggregationResult = new ArrayList<>();
            salesCollection.aggregate(aggregationPipeline)
                    .allowDiskUse(true)
                    .into(aggregationResult);

// Process the results
            for (Document resultDocument : aggregationResult) {
                totalSummary.setTotalPaid(resultDocument.getDouble("totalPaid"));
                totalSummary.setRemaining(resultDocument.getDouble("remaining"));
                totalSummary.setTotalPrice(resultDocument.getDouble("totalPrice"));
                totalSummary.setTotalQuantity(resultDocument.getInteger("totalQuantity", 0));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalSummary;
    }



    public List<Sales> getAllSales() {
        List<Sales> salesList = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> salesDocument = database.getCollection("Sales");

            if (salesDocument != null) {
                // Retrieve all sale documents from the collection
                FindIterable<Document> saleDocuments = salesDocument.find();

                for (Document saleDocument : saleDocuments) {
                    Sales sale = new Sales();
                    sale.setId(saleDocument.getInteger("id"));
                    sale.setTotalPrice(saleDocument.getDouble("totalPrice"));
                    sale.setPaid(saleDocument.getDouble("totalPaid"));
                    sale.setRemaining(saleDocument.getDouble("remaining"));
                    // Decode LocalDate and LocalTime
                    sale.setSaleDate(saleDocument.getDate("saleDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    sale.setSaleTime(saleDocument.getDate("saleTime").toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

                    // Decode the products list
                    List<Products> productsList = decodeProductsList(saleDocument.getList("products", Document.class));
                    sale.setProducts(productsList);

                    salesList.add(sale);
                }
            } else {
                System.err.println("Error: Sales collection is null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salesList;
    }

    private List<Products> decodeProductsList(List<Document> productsDocuments) {
        List<Products> productsList = new ArrayList<>();

        for (Document productDocument : productsDocuments) {
            Products product = Products.fromDocument(productDocument);
            productsList.add(product);
        }

        return productsList;
    }
    public int getLatestSaleIdFromDB() {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var collection = database.getCollection("Sales");

            // Sort in descending order based on the sale ID and get the first document
            Document latestSaleDocument = collection.find()
                    .sort(descending("id"))
                    .limit(1)
                    .first();

            if (latestSaleDocument != null) {
                // Extract the saleId field from the document
                return latestSaleDocument.getInteger("id", 0) + 1;
            } else {
                // No sale documents found, return a starting sale ID
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
            return 0;
        }
    }
}