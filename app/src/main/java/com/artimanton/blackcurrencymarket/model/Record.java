package com.artimanton.blackcurrencymarket.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Record  implements Serializable {
    public String data;
    public String time;
    public String sell_buy;
    public String city;
    public String currency;
    public String price;
    public String kol;
    public String phone;
    public String key;

    public Record(){

    }

    public Record(String data, String time, String sell_buy, String city, String currency,  String price, String kol, String phone, String key) {
        this.data = data;
        this.time = time;
        this.city = city;
        this.sell_buy = sell_buy;
        this.currency = currency;
        this.price = price;
        this.kol = kol;
        this.phone = phone;
        this.key = key;
    }


    @Override
    public String toString() {
        return "Record{" +
                "data='" + data + '\'' +
                ", time='" + time + '\'' +
                ", sell_buy='" + sell_buy + '\'' +
                ", city='" + city + '\'' +
                ", currency='" + currency + '\'' +
                ", price='" + price + '\'' +
                ", kol='" + kol + '\'' +
                ", phone='" + phone + '\'' +
                ", key='" + key + '\'' +
                '}';
    }


    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("time", time);
        result.put("sell_buy", sell_buy);
        result.put("city", city);
        result.put("currency", currency);
        result.put("price", price);
        result.put("kol", kol);
        result.put("phone", phone);
        result.put("key", key);
        return result;
    }
}
