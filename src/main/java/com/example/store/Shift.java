package com.example.store;

import com.example.store.GUI.Cashier.CashierController;
import com.example.store.Sales.GetSalesDocument;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

public class Shift {

    private String username;
    private LocalDate beginLocalDate;
    private LocalTime beginLocalTime;
    private LocalDate endLocalDate;
    private LocalTime endLocalTime;
    private Double totalMoney;
    private Integer id;
    private Double total;

    public Shift(String username, LocalDate beginLocalDate, LocalTime beginLocalTime, LocalDate endLocalDate, LocalTime endLocalTime, Double totalMoney) {
        this.username = username;
        this.beginLocalDate = beginLocalDate;
        this.beginLocalTime = beginLocalTime;
        this.endLocalDate = endLocalDate;
        this.endLocalTime = endLocalTime;
        this.totalMoney = totalMoney;
    }
    public Shift(String username, LocalDate beginLocalDate, LocalTime beginLocalTime, LocalDate endLocalDate, LocalTime endLocalTime, Double totalMoney ,Integer id, Double total) {
        this.username = username;
        this.id = id;
        this.beginLocalDate = beginLocalDate;
        this.beginLocalTime = beginLocalTime;
        this.endLocalDate = endLocalDate;
        this.endLocalTime = endLocalTime;
        this.totalMoney = totalMoney;
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shift() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBeginLocalDate() {
        return beginLocalDate;
    }

    public void setBeginLocalDate(LocalDate beginLocalDate) {
        this.beginLocalDate = beginLocalDate;
    }

    public LocalTime getBeginLocalTime() {
        return beginLocalTime;
    }

    public void setBeginLocalTime(LocalTime beginLocalTime) {
        this.beginLocalTime = beginLocalTime;
    }

    public LocalDate getEndLocalDate() {
        return endLocalDate;
    }

    public void setEndLocalDate(LocalDate endLocalDate) {
        this.endLocalDate = endLocalDate;
    }

    public LocalTime getEndLocalTime() {
        return endLocalTime;
    }

    public void setEndLocalTime(LocalTime endLocalTime) {
        this.endLocalTime = endLocalTime;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
    public boolean addShift(Shift shift) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> shiftsCollection = database.getCollection("Shifts");

            // Check if the new shift's ID matches the ID of the latest shift in the database
            Integer latestShiftId = getLatestShiftId(); // Assuming you have a method to retrieve the latest shift ID
            Document filter = new Document("id", shift.getId());

            // Find the document using the filter
            Document result = shiftsCollection.find(filter).first();
            if(result == null)
            {
                // Convert Shift object to Document
                Document shiftDocument = new Document()
                        .append("id", 1)
                        .append("username", shift.getUsername())
                        .append("beginLocalDate", shift.getBeginLocalDate() != null ? shift.getBeginLocalDate().toString() : null)
                        .append("beginLocalTime", shift.getBeginLocalTime() != null ? shift.getBeginLocalTime().toString() : null)
                        .append("endLocalDate", shift.getEndLocalDate() != null ? shift.getEndLocalDate().toString() : null)
                        .append("endLocalTime", shift.getEndLocalTime() != null ? shift.getEndLocalTime().toString() : null)
                        .append("totalMoney", shift.getTotalMoney())
                        .append("total", 0.0);
                shiftsCollection.insertOne(shiftDocument);
                return true;
            }
            if (shift.getId() == latestShiftId && result.getDouble("totalMoney") == null) {
                // The new shift's ID is the same as the latest shift's ID, return false
                return false;
            }

            // Convert Shift object to Document
            Document shiftDocument = new Document()
                    .append("id", shift.getId() + 1)
                    .append("username", shift.getUsername())
                    .append("beginLocalDate", shift.getBeginLocalDate() != null ? shift.getBeginLocalDate().toString() : null)
                    .append("beginLocalTime", shift.getBeginLocalTime() != null ? shift.getBeginLocalTime().toString() : null)
                    .append("endLocalDate", shift.getEndLocalDate() != null ? shift.getEndLocalDate().toString() : null)
                    .append("endLocalTime", shift.getEndLocalTime() != null ? shift.getEndLocalTime().toString() : null)
                    .append("totalMoney", shift.getTotalMoney())
                    .append("total", 0.0);

            // Insert the document into the collection
            shiftsCollection.insertOne(shiftDocument);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false in case of an exception
    }


    public boolean editShift(Shift shift, String name, Integer shiftnum) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> shiftsCollection = database.getCollection("Shifts");

            // Create a filter based on username
            Document filterbynameandid = new Document("username", name).append("id", shiftnum);
            Document filterbyid = new Document("id", shiftnum);
            // if shift is not found return false
            long shiftCount = shiftsCollection.countDocuments(filterbyid);
            if (shiftCount == 0) {
                return false; // Shift not present, return false
            }

            // Create an update document
            Document updateDocument = new Document();

            // Add non-null fields to the update document
            Shift latestshift = new Shift();
            GetSalesDocument getSalesDocument = new GetSalesDocument();
            latestshift = latestshift.getShiftByNumber(shiftnum);

            if (shift.getEndLocalDate() != null) {
                updateDocument.append("endLocalDate", shift.getEndLocalDate().toString());
            }
            if (shift.getEndLocalTime() != null) {
                updateDocument.append("endLocalTime", shift.getEndLocalTime().toString());
            }
            if (shift.getTotalMoney() != null) {
                updateDocument.append("totalMoney", shift.getTotalMoney());
            }
            if(shift.getTotal() == 0.0)
            {
                updateDocument.append("total", getSalesDocument.getTotalSummaryTime(latestshift.getBeginLocalDate(), shift.getEndLocalDate(), latestshift.getBeginLocalTime(), shift.getEndLocalTime()));
            }

            // Exclude the "_id" field from the update
            updateDocument.remove("_id");
            // if the worker did not open a shift with his name and id and wants to close a shift
            long shiftCount1 = shiftsCollection.countDocuments(filterbynameandid);
            /*if(shiftCount1 == 0)
            {
                Shift latestshift = new Shift();
                latestshift = latestshift.getShiftByNumber(shiftnum);
                updateDocument.append("id", latestshift.getId() + 1);
                updateDocument.append("username", shift.getUsername());
                updateDocument.append("beginLocalDate", latestshift.endLocalDate.toString());
                updateDocument.append("beginLocalTime", latestshift.endLocalTime.toString());
                updateDocument.append("endLocalDate", shift.endLocalDate.toString());
                updateDocument.append("endLocalTime", shift.endLocalTime.toString());
                updateDocument.append("totalMoney", shift.totalMoney);
                updateDocument.append("total", getSalesDocument.getTotalSummaryTime(latestshift.getBeginLocalDate(), shift.getEndLocalDate(), latestshift.getBeginLocalTime(), shift.getEndLocalTime()));
                shiftsCollection.insertOne(updateDocument);
                return true;
            }*/
            // Perform the update operation
            shiftsCollection.updateOne(filterbynameandid, new Document("$set", updateDocument));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Shift getShiftByNumber(Integer shiftNumber) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> shiftsCollection = database.getCollection("Shifts");
            // Create a filter based on both username and shiftNumber
            Document filter = new Document("id", shiftNumber);

            // Find the document using the filter
            Document result = shiftsCollection.find(filter).first();

            LocalDate endLocalDate = null;
            LocalTime endLocalTime = null;

            // Convert the document to a Shift object
            String beginLocalDateStr = result.getString("beginLocalDate");
            String beginLocalTimeStr = result.getString("beginLocalTime");
            String endLocalDateStr = result.getString("endLocalDate");
            String endLocalTimeStr = result.getString("endLocalTime");

            // Check if endLocalDateStr is not null before parsing
            if (endLocalDateStr != null) {
                endLocalDate = LocalDate.parse(endLocalDateStr);
            }

            // Check if endLocalTimeStr is not null before parsing
            if (endLocalTimeStr != null) {
                endLocalTime = LocalTime.parse(endLocalTimeStr);
            }


            // Convert String to LocalDate
            LocalDate beginLocalDate = LocalDate.parse(beginLocalDateStr);

            // Convert String to LocalTime
            LocalTime beginLocalTime = LocalTime.parse(beginLocalTimeStr);


            return new Shift(
                    result.getString("username"),
                    beginLocalDate,
                    beginLocalTime,
                    endLocalDate,
                    endLocalTime,
                    result.getDouble("totalMoney"),
                    result.getInteger("id"),
                    result.getDouble("total")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int UpdateShiftId() {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var collection = database.getCollection("Shifts");

            // Find the sale document with the given ID
            Document shiftDocument = collection.find().sort(descending("id")).limit(1).first();
            if (shiftDocument != null) {
                // Extract the saleId field from the document
                int latestShiftId = shiftDocument.getInteger("id", 0);

                // Increment the ID
                return latestShiftId + 1;
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

    public int getLatestShiftId() {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("KhanMariaStore");
            var collection = database.getCollection("Shifts");

            // Find the latest Shift document
            Document latestShiftDocument = collection.find().sort(descending("id")).limit(1).first();

            if (latestShiftDocument != null) {
                // Extract the Shift ID field from the document
                int latestShiftId = latestShiftDocument.getInteger("id", 0);

                // Increment the ID
                return latestShiftId;
            } else {
                // No Shift documents found, return a starting Shift ID
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
            return 0;
        }
    }

    public List<Shift> getShiftsByDate(LocalDate targetDate) {
        List<Shift> shifts = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Specify the database
            MongoDatabase database = mongoClient.getDatabase("KhanMariaStore");

            // Specify the collection
            MongoCollection<Document> shiftsCollection = database.getCollection("Shifts");

            // Create a filter based on the date
            Document filter = new Document("beginLocalDate", targetDate.toString());

            // Find the documents using the filter
            FindIterable<Document> results = shiftsCollection.find(filter);

            // Iterate through the results and convert each document to a Shift object
            for (Document result : results) {
                String beginLocalDateStr = result.getString("beginLocalDate");
                String beginLocalTimeStr = result.getString("beginLocalTime");
                String endLocalDateStr = result.getString("endLocalDate");
                String endLocalTimeStr = result.getString("endLocalTime");

                // Convert String to LocalDate
                LocalDate beginLocalDate = LocalDate.parse(beginLocalDateStr);

                // Convert String to LocalTime
                LocalTime beginLocalTime = LocalTime.parse(beginLocalTimeStr);

                // Convert String to LocalDate
                LocalDate endLocalDate = LocalDate.parse(endLocalDateStr);

                // Convert String to LocalTime
                LocalTime endLocalTime = LocalTime.parse(endLocalTimeStr);

                Shift shift = new Shift(
                        result.getString("username"),
                        beginLocalDate,
                        beginLocalTime,
                        endLocalDate,
                        endLocalTime,
                        result.getDouble("totalMoney"),
                        result.getInteger("id"),
                        result.getDouble("total")
                );
                shifts.add(shift);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shifts;
    }



}
