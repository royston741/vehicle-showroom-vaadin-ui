package com.example.application.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Page<T> {

    private List<T> content=new ArrayList<>();

    private Pageable pageable;

    private int totalPages;

    private boolean last;

    private boolean first;
}
