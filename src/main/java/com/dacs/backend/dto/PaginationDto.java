package com.dacs.backend.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PaginationDto<T> {
    private int pagina;
    private int size;

    @Getter
    @Setter
    public static class Response<T> extends PaginationDto<T> 
    {
        private long totalElementos;
        private int totalPaginas;
        private List<T> contenido;
    }
}
