package com.example.application.service;

import com.example.application.constants.SortDirection;
import com.example.application.constants.TwoWheelerType;
import com.example.application.constants.VehicleType;
import com.example.application.dto.MiscellaneousCost;
import com.example.application.dto.Page;
import com.example.application.dto.Response;
import com.example.application.dto.VehicleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "vehicleapi", url = "${vehicle.url}")
public interface VehicleService {

    @GetMapping("getAllVehicles")
    public ResponseEntity<Response<Page<VehicleDTO>>> getAllVehicles(
            @RequestParam String column,
            @RequestParam SortDirection direction,
            @RequestParam VehicleType vehicleType,
            @RequestParam TwoWheelerType twoWheelerType,
            @RequestParam Double startPrice,
            @RequestParam Double endPrice,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String filterValue
    );

    @GetMapping("getMaxAndMinPrice")
    public ResponseEntity<Response<List<Double>>> getMaxAndMinPrice();

    @GetMapping("getDiscountByFuelType")
    public ResponseEntity<Response<List<MiscellaneousCost>>> getDiscountByFuelType();

    @GetMapping("getDiscountByTwoWheelerType")
    public ResponseEntity<Response<List<MiscellaneousCost>>> getDiscountByTwoWheelerType();

    @GetMapping("getVehicleType")
    public ResponseEntity<Response<List<String>>> getVehicleType();

}
