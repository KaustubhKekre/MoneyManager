package com.phantomorion.moneymanager;

public class transaction {
    String amount,date,type,description;
    public transaction(){}

    public transaction(String amount, String date, String type, String description) {
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}