package com.example.application.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

	private boolean isSuccess = false;

	private List<String> errMssg = new ArrayList<>();

	private T responseData;



}
