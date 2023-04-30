package com.example.udhari;

public class _2_TransactionInfo {

    private String date;
    private String time;
    private Long amount;
    private String info;
    public _2_TransactionInfo(){}


    public _2_TransactionInfo(String date,String time, Long amount, String info) {
        this.date = date;
        this.time=time;
        this.amount = amount;
        this.info = info;
    }
    public void copy(_2_TransactionInfo temp) {
        this.date = temp.date;
        this.time=temp.time;
        this.amount = temp.amount;
        this.info = temp.info;
    }

    public String getTime() { return time;}

    public void setTime(String time) { this.time = time; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
