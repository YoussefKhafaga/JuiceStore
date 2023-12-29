package com.example.store.Product;

import org.bson.Document;

public class Products {
    private String id;
    private String productName;
    private Double productPrice;
    private String category;
    private String description;
    private Integer productQuantity;

    // Constructors, getters, setters, etc.

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

    public Products(String productName, Double productPrice, Integer productQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public Document toDocument() {
        return new Document()
                .append("productName", productName)
                .append("productPrice", productPrice)
                .append("category", category)
                .append("description", description);
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

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
}