package com.example.store.Product;

import org.bson.Document;

public class Products {
    private String id = "";
    private String productName = "";
    private Double productPrice = 0.0;
    private String category = "";
    private String description = "";
    private Double productQuantity = 0.0;

    // Constructors, getters, setters, etc.


    public Products(String productName) {
        this.productName = productName;
    }

    public Products() {
    }

    public Products(String id, String productName, Double productPrice, String category) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.category = category;
    }

    public Products(String productName, Double productPrice, String category, String description) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.category = category;
        this.description = description;
    }

    public Products(String productName, Double productPrice, Double productQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public Document toDocument() {
        return new Document()
                .append("productName", productName)
                .append("productPrice", productPrice)
                .append("category", category)
                .append("quantity", productQuantity)
                .append("description", description);
    }

    public static Products fromDocument(Document document) {
        String productName = document.getString("productName");
        double productPrice = document.getDouble("productPrice");
        double productQuantity = document.getDouble("productQuantity"); // Corrected field name

        return new Products(productName, productPrice, productQuantity);
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.productPrice = price;
    }

    public Double getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Double productQuantity) {
        this.productQuantity = productQuantity;
    }
}