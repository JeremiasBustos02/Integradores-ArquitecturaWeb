package com.monopatines.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private boolean first;
    private boolean last;
    private boolean empty;
}

