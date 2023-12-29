package com.example.store.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBConnector {

    private static final String MONGODB_URI = "mongodb://localhost:27017"; // Replace with your MongoDB URI

    public static MongoClient connect() {
        return MongoClients.create(MONGODB_URI);
    }
}
