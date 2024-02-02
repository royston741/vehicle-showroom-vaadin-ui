package com.example.application.dto;


import com.example.application.constants.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDTO {

	private Integer id;

	@NotBlank(message = "First name must not be empty")
	@Size(min = 3, message = "first name should be of at least of 3 letters")
	private String firstName;

	@NotBlank(message = "Last name must not be empty")
	@Size(min = 3, message = "last name should be of at least of 3 letters")
	private String lastName;

	@NotBlank(message = "Email must not be empty")
	@Email(regexp = "[a-z0-9$&+,:;=?@#|'<>.^*()%!-]+@[a-z]+\\.[a-z]{2,3}", message = "Email is not valid")
	private String email;

	@NotBlank(message = "Phone no. must not be empty")
	@Size(min = 10, max = 10, message = "Phone no should be of length 10")
	private String phoneNo;

	@NotBlank(message = "Address must not be empty")
	@Size(min = 3, message = "address should be of at least of 3 letters")
	private String address;

	@NotBlank(message = "Password must not be empty")
	@Size(min = 8, message = "password should be of at least of 8 letters")
	private String password;

	private UserType userType;


	// changea

}
