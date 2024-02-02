package com.example.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageConfig {

    private int pageNumber;
    private int pageSize=12;
}
