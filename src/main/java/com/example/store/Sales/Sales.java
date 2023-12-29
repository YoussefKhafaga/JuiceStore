package com.example.store.Sales;

import com.example.store.Product.Products;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Sales {
    private String id;
    private List<Products> products;
    private double totalPrice;
    private LocalDate saleDate;
    private LocalTime saleTime;

    public Sales(String id, List<Products> products, double totalPrice, LocalDate saleDate, LocalTime saleTime) {
        this.id = id;
        this.products = products;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
        this.saleTime = saleTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public LocalTime getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(LocalTime saleTime) {
        this.saleTime = saleTime;
    }
}
