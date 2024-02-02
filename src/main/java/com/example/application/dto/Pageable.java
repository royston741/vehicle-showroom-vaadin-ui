package com.example.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pageable {
    private int pageNumber;
    private int pageSize;
}
