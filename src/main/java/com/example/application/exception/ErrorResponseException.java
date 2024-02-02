package com.example.application.exception;

import com.example.application.dto.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseException extends RuntimeException{

    private Response<Object> response=new Response<>();

}
