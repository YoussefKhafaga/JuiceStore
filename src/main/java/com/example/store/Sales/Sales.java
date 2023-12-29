package com.example.store.Sales;

import org.bson.Document;

public class Sales {
    private String id;
    private String productName;
    private double productPrice;
    private int quantity;
    private double total;

    // Constructors, getters, setters, etc.

    public Sales(String id, String productName, double productPrice, int quantity, double total) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.total = total;
    }

    public Sales(String productName, double productPrice, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Document toDocument() {
        return new Document()
                .append("productName", productName)
                .append("ProductPrice", productPrice)
                .append("quantity", quantity)
                .append("total", total);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
