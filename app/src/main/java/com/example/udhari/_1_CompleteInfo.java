package com.example.udhari;

import java.util.ArrayList;

public class _1_CompleteInfo {

    private String name;
    private String number;
    private Long amount;
    public ArrayList<_2_TransactionInfo>transaction=new ArrayList<>();
    public _1_CompleteInfo() {}

    public _1_CompleteInfo(String name, String number, Long amount, ArrayList<_2_TransactionInfo> transaction) {
        this.name = name;
        this.number = number;
        this.amount = amount;
        this.transaction = transaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public ArrayList<_2_TransactionInfo> getTransaction() {
        return transaction;
    }

    public void setTransaction(ArrayList<_2_TransactionInfo> transaction) {
        this.transaction = transaction;
    }
}
