package com.capgemini.Hospital_Management_UI.dto;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class Response<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

 
}
