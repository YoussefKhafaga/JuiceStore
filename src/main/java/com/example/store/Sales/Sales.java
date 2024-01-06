package com.example.store.Sales;

import com.example.store.Product.Products;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Sales {
    private Integer id;
    private List<Products> products;
    private double totalPrice;
    private double remaining;
    private  double paid;

    private LocalDate saleDate;
    private LocalTime saleTime;
    private Integer shiftnumber;

    public Sales(Integer id, List<Products> products, double totalPrice, double paid, double remaining, LocalDate saleDate, LocalTime saleTime) {
        this.id = id;
        this.products = products;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.remaining = remaining;
        this.saleDate = saleDate;
        this.saleTime = saleTime;
    }

    public Sales() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShiftnumber() {
        return shiftnumber;
    }

    public void setShiftnumber(Integer shiftnumber) {
        this.shiftnumber = shiftnumber;
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

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }
}
