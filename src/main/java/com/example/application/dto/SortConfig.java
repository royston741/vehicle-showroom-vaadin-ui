package com.example.application.dto;


import com.example.application.constants.SortDirection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SortConfig {

   private SortDirection direction= SortDirection.ASC;

   private  String column="id";

}
