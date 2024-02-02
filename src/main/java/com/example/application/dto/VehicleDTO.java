package com.example.application.dto;


import com.example.application.constants.TwoWheelerType;
import com.example.application.constants.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleDTO {


	private Integer id;

	@NotBlank(message = "Vehicle name must not be empty")
	@Size(min = 3, message = "Vehicle name should be of at least of 3 letters")
	private String name;

	private Double price;

	private double rating;

	private VehicleType vehicleType;

	private TwoWheelerType twoWheelerType;

	private String description;

	private String 	vehicleImg;
}
