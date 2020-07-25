package com.syntexerror.outdoor_bd;

public class test_model {

    String  item_name
            ,item_price
            ,item_URL
            ,item_type_code
            ,createdAt;

    public test_model(String item_name, String item_price, String item_URL, String item_type_code, String createdAt) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_URL = item_URL;
        this.item_type_code = item_type_code;
        this.createdAt = createdAt;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_URL() {
        return item_URL;
    }

    public void setItem_URL(String item_URL) {
        this.item_URL = item_URL;
    }

    public String getItem_type_code() {
        return item_type_code;
    }

    public void setItem_type_code(String item_type_code) {
        this.item_type_code = item_type_code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}