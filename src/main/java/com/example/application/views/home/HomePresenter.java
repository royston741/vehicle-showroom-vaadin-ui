package com.example.application.views.home;


import com.example.application.dto.*;
import com.example.application.service.VehicleService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@Component
public class HomePresenter {

    @Autowired
    private VehicleService vehicleService;

    public List<String> getAllTwoWheelerType() {
        List<String> twoWheelerTypeList = null;
        try {
            twoWheelerTypeList = Objects.requireNonNull(vehicleService.getDiscountByTwoWheelerType().getBody()).getResponseData().stream().map(twoWheeler -> twoWheeler.getName()).toList();
        } catch (NullPointerException e) {
            twoWheelerTypeList = new ArrayList<>();
        } catch (Exception e) {
            twoWheelerTypeList = new ArrayList<>();
            log.info("Error in getAllTwoWheelerType {} ", e);
        }
        return twoWheelerTypeList;
    }

    public List<String> getAllVehicleType() {
        List<String> vehicleTypeList = null;
        try {
            vehicleTypeList = Objects.requireNonNull(vehicleService.getVehicleType().getBody()).getResponseData();
        } catch (NullPointerException e) {
            vehicleTypeList = new ArrayList<>();
        } catch (Exception e) {
            vehicleTypeList = new ArrayList<>();
            log.info("Error in getVehicleType {} ", e);
        }
        return vehicleTypeList;
    }

    public List<Double> getStartAndEndPrice() {
        List<Double> minAndMaxPrice = null;
        try {
            minAndMaxPrice = Objects.requireNonNull(vehicleService.getMaxAndMinPrice().getBody()).getResponseData();
        } catch (NullPointerException e) {
            minAndMaxPrice = new ArrayList<>();
        } catch (Exception e) {
            minAndMaxPrice = new ArrayList<>();
            log.info("Error in getStartAndEndPrice {} ", e);
        }
        return minAndMaxPrice;
    }

    public Page<VehicleDTO> getAllAvailableVehicles(int pageNumber, int pageSize, SortConfig sortConfig, FilterConfig filterConfig) {
        Page<VehicleDTO> vehicleList = null;
        try {
            vehicleList = Objects.requireNonNull(vehicleService.getAllVehicles(
                    sortConfig.getColumn(),
                    sortConfig.getDirection(),
                    filterConfig.getVehicleType(),
                    filterConfig.getTwoWheelerType(),
                    filterConfig.getStartingPrice(),
                    filterConfig.getEndingPrice(),
                    pageNumber,
                    pageSize,
                    filterConfig.getFilterValue()
            ).getBody()).getResponseData();
        } catch (NullPointerException e) {
            vehicleList = new Page<>();
        } catch (Exception e) {
            vehicleList = new Page<>();
            log.info("Error in getAllAvailableVehicles {} ", e);
        }
        return vehicleList;

    }
}
