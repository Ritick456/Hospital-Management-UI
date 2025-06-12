package com.capgemini.Hospital_Management_UI.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response<T> {
    private int status;
    private String message;
    private LocalDateTime time;
    private T data;
}





