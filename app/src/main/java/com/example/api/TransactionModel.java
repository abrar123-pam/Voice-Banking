package com.example.api;

public class TransactionModel {
    String amount;
    String nickname;
    String date;
    String time;

//    public transactionmodel(String amount, String nickname, String date, String time) {
//        this.amount = amount;
//        this.nickname = nickname;
//        this.date = date;
//        this.time = time;
//    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
