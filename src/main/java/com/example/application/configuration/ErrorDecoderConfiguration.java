package com.example.application.configuration;



import com.example.application.utills.RetrieveErrorResponse;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorDecoderConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new RetrieveErrorResponse();
    }
}
