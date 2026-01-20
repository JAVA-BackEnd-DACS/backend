package com.dacs.backend.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PaginationDto<T> {
    private int number;
    private int size;

    @Getter
    @Setter
    public static class Response<T> extends PaginationDto<T> 
    {
        private long totalElements;
        private int totalPages;
        private List<T> content;
    }
}
