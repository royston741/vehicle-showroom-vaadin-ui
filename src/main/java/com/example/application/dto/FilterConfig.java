package com.example.application.dto;


import com.example.application.constants.TwoWheelerType;
import com.example.application.constants.VehicleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilterConfig {
    private VehicleType vehicleType;
    private TwoWheelerType twoWheelerType;
    private Double startingPrice=0d;
    private Double endingPrice=0d;
    private String filterValue="";
}
