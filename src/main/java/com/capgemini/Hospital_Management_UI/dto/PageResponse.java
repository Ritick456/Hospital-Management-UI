package com.capgemini.Hospital_Management_UI.dto;

import lombok.Data;

import java.util.List;


@Data
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int totalPages;
    // Include other fields if your backend sends them
}

