package com.example.LoginPractice.config;

import com.example.LoginPractice.controller.dto.response.FailResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionControllerAdvice {

    @ExceptionHandler(CheckErrorException.class)
    public ResponseEntity<FailResponse> handleException(CheckErrorException e) {
        FailResponse failResponse = new FailResponse();
        failResponse.setStatusCode(e.getStatusCode());
        failResponse.setStatusMsg(e.getStatusMsg());
        return ResponseEntity.status(HttpStatus.OK).body(failResponse);
    }

    // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponse> handler(MethodArgumentNotValidException e) {

        FailResponse failResponse = new FailResponse();
        StringBuilder sb = new StringBuilder();

        List<FieldError> fieldErrors=e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage()).append("  ");
        }
        failResponse.setStatusMsg(sb.toString());

        return new ResponseEntity<FailResponse>(failResponse, HttpStatus.BAD_REQUEST);
    }
}
