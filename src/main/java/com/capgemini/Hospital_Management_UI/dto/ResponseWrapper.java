package com.capgemini.Hospital_Management_UI.dto;




import java.util.List;


public class ResponseWrapper<T> {
    private String timestamp;
    private int status;
    private String message;
    private List<T> data;


    public ResponseWrapper(String timestamp, List<T> data, String message, int status) {
        this.timestamp = timestamp;
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }
}


