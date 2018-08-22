package com.vitartha.hisabkitab.Class;

import org.json.JSONObject;

public class DebitDetails {

    String name, mode, comment, date, category,amount;
    int id;


    public DebitDetails(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.name = jsonObject.optJSONObject("contact").optString("name");
        this.mode = jsonObject.optJSONObject("mode").optString("mode");
        this.date = jsonObject.optString("transaction_date");
        this.category = jsonObject.optString("category");
        this.amount = jsonObject.optString("amount");
        this.comment = jsonObject.optString("comments");
    }

   /* public DebitDetails(String name, String mode, String comment, String date, String category, long amount) {
        this.name = name;
        this.mode = mode;
        this.category = category;
        this.comment = comment;
        this.date = date;
        this.amount = amount;
    }*/

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public String getComment() {
        return comment;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
    return id;
    }

    public String getCategory() {
        return category;
    }


}