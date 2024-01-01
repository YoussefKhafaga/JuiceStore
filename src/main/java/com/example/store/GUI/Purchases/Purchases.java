package com.example.store.GUI.Purchases;

import com.mongodb.internal.connection.Time;

import java.time.LocalDate;
import java.util.Date;

public class Purchases {
    private String purchaseName;
    private Double purchasePrice;
    private LocalDate date;
    private Time time;

    public Purchases(String purchaseName, Double purchasePrice, LocalDate date, Time time) {
        this.purchaseName = purchaseName;
        this.purchasePrice = purchasePrice;
        this.date = date;
        this.time = time;
    }

    public Purchases(String purchaseName, Double purchasePrice, LocalDate date) {
        this.purchaseName = purchaseName;
        this.purchasePrice = purchasePrice;
        this.date = date;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
