package com.artimanton.blackcurrencymarket.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Record  implements Serializable {
    public String data;
    public String price;
    public String kol;
    public String phone;
    public String key;

    public Record(){

    }

    public Record(String data, String price, String kol, String phone, String key) {
        this.data = data;
        this.price = price;
        this.kol = kol;
        this.phone = phone;
        this.key = key;
    }

    @Override
    public String toString() {
        return "Record{" +
                "data='" + data + '\'' +
                ", price='" + price + '\'' +
                ", kol='" + kol + '\'' +
                ", phone='" + phone + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("price", price);
        result.put("kol", kol);
        result.put("phone", phone);
        result.put("key", key);
        return result;
    }
}
