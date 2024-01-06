package com.example.store.Sales;

import com.example.store.Product.Products;
import com.mongodb.client.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
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

            // Match documents within the date range and count them
            var matchPipeline = Arrays.asList(
                    match(and(
                            gte("saleDate", start),
                            lt("saleDate", end)
                    )),
                    group(0.0,
                            sum("totalSales", (1)),
                            sum("totalPrice", "$totalPrice")
                    )
            );

            // Combine the two lists
            List<Bson> aggregationPipeline = new ArrayList<>();
            aggregationPipeline.addAll(matchPipeline);

            // Execute the aggregation pipeline
            List<Document> aggregationResult = new ArrayList<>();
            salesCollection.aggregate(aggregationPipeline)
                    .allowDiskUse(true)
                    .into(aggregationResult);

            // Process the results
            if (!aggregationResult.isEmpty()) {
                totalSummary.setTotalQuantity(aggregationResult.get(0).getInteger("totalSales"));
                totalSummary.setTotalPrice(aggregationResult.get(0).getDouble("totalPrice"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalSummary;
    }

    /*public Double getTotalSummaryTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var salesCollection = database.getCollection("Sales");

            // Convert LocalDate and LocalTime to Date
            Date startDateTime = Date.from(LocalDateTime.of(startDate, startTime).atZone(ZoneId.systemDefault()).toInstant());
            Date endDateTime = Date.from(LocalDateTime.of(endDate, endTime).atZone(ZoneId.systemDefault()).toInstant());

            // Match documents within the date and time range
            var matchPipeline = Arrays.asList(
                    match(and(
                            gte("saleDate", startDateTime),
                            lt("saleDate", endDateTime)
                    )),
                    group(0.0,
                            sum("totalPrice", "$totalPrice")
                    )
            );

            // Combine the two lists
            List<Bson> aggregationPipeline = new ArrayList<>();
            aggregationPipeline.addAll(matchPipeline);

            // Execute the aggregation pipeline
            List<Document> aggregationResult = new ArrayList<>();
            salesCollection.aggregate(aggregationPipeline)
                    .allowDiskUse(true)
                    .into(aggregationResult);

            // Process the results
            if (!aggregationResult.isEmpty()) {
                return aggregationResult.get(0).getDouble("totalPrice");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }
*/

    public Double getTotalByShift(int shiftNumber) {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var salesCollection = database.getCollection("Sales");

            // Match documents by shift number
            var matchPipeline = Arrays.asList(
                    match(eq("shiftNumber", shiftNumber)),
                    group(0.0,
                            sum("totalPrice", "$totalPrice")
                    )
            );

            // Combine the two lists
            List<Bson> aggregationPipeline = new ArrayList<>();
            aggregationPipeline.addAll(matchPipeline);

            // Execute the aggregation pipeline
            List<Document> aggregationResult = new ArrayList<>();
            salesCollection.aggregate(aggregationPipeline)
                    .allowDiskUse(true)
                    .into(aggregationResult);

            // Process the results
            if (!aggregationResult.isEmpty()) {
                return aggregationResult.get(0).getDouble("totalPrice");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }


    public TotalSummary getTotalSummaryDay(LocalDate date) {
        TotalSummary totalSummary = new TotalSummary();

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var salesCollection = database.getCollection("Sales");

            // Match documents within the date range
            Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endOfDay = Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            var matchPipeline = Arrays.asList(
                    match(and(
                            gte("saleDate", startOfDay),
                            lt("saleDate", endOfDay)
                    )),
                    group(0.0,
                            sum("totalSales", 1),
                            sum("totalPrice", "$totalPrice")
                    )
            );


            // Combine the two lists
            List<Bson> aggregationPipeline = new ArrayList<>();
            aggregationPipeline.addAll(matchPipeline);

            // Execute the aggregation pipeline
            List<Document> aggregationResult = new ArrayList<>();
            salesCollection.aggregate(aggregationPipeline)
                    .allowDiskUse(true)
                    .into(aggregationResult);

            // Process the results
            if (!aggregationResult.isEmpty()) {
                totalSummary.setTotalQuantity(aggregationResult.get(0).getInteger("totalSales"));
                totalSummary.setTotalPrice(aggregationResult.get(0).getDouble("totalPrice"));
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

            // Get today's date
            LocalDate today = LocalDate.now();

            // Find the latest sale document for today
            Document latestSaleDocument = collection.find(and(
                            eq("saleDate", today)))
                    .sort(descending("id"))
                    .limit(1)
                    .first();
            if (latestSaleDocument != null) {
                // Extract the saleId field from the document
                int latestSaleId = latestSaleDocument.getInteger("id");

                // Increment the ID
                return latestSaleId + 1;
            } else {
                // No sale documents found for today, return a starting sale ID
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
            return 0;
        }
    }


}
