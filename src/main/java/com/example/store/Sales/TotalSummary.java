package com.example.store.Sales;

public class TotalSummary {
    private double totalPaid;
    private double remaining;
    private double totalPrice;
    private Integer TotalQuantity;
    private double purchases;

    public double getPurchases() {
        return purchases;
    }

    public void setPurchases(double purchases) {
        this.purchases = purchases;
    }

    public TotalSummary(double totalPaid, double remaining, double totalPrice, Integer TotalQuantity, double purchases) {
        this.totalPaid = totalPaid;
        this.remaining = remaining;
        this.totalPrice = totalPrice;
        this.TotalQuantity = TotalQuantity;
    }

    public TotalSummary() {
    }

    public Integer getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // getters and setters

    // constructor
}

