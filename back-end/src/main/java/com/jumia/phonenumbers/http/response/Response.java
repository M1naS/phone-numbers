package com.jumia.phonenumbers.http.response;

import lombok.Data;

import java.util.HashMap;

@Data
public class Response<T> {
    private T data;
    private HashMap<String, Object> metaData;

    public Response() {
        this.metaData = new HashMap<>();
    }

    public void addMetaData(String key, Object value) {
        this.metaData.put(key, value);
    }
}
