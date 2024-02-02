package com.example.application.utills;

import com.example.application.exception.ErrorResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

// When errors occur, the Feign client suppresses the original message,
// and to retrieve it, we require to write a custom ErrorDecoder.
public class RetrieveErrorResponse implements ErrorDecoder{

    @Override
    public Exception decode(String methodKey, Response response) {
        com.example.application.dto.Response<Object> message = null;
        //deserialize the response body
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
                message = mapper.readValue(bodyIs, com.example.application.dto.Response.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        return new ErrorResponseException(message);
    }
}
