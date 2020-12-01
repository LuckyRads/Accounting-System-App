package accountingsystem.app.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {

    private long id;

    private String name;

    private TransactionType transactionType;

    private String sender;

    private String receiver;

    private double amount;

    private LocalDate date;

    private Category category;

    public Transaction() {

    }

    public Transaction(String name, TransactionType transactionType, String sender, String receiver, double amount, LocalDate date, Category category) {
        this.name = name;
        this.transactionType = transactionType;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return this.transactionType.toString() + ": " + this.name;
    }

}
