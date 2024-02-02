package com.example.application.service;

import com.example.application.dto.CustomerDTO;
import com.example.application.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="userapi",url = "${user.url}")
public interface UserService {
    @GetMapping("logIn")
    public ResponseEntity<Response<CustomerDTO>> logIn(@RequestParam String name, @RequestParam String password);

    @PostMapping("createCustomer")
    public ResponseEntity<Response<CustomerDTO>> createCustomer(CustomerDTO customer);

    @GetMapping("getCustomerById")
    public ResponseEntity<Response<CustomerDTO>> getCustomerById(@RequestParam(name = "id") int customerId);

}
