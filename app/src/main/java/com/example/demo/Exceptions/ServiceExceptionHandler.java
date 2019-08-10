package com.example.demo.Exceptions;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(value = { BookServiceException.BookCantExtend.class
                ,BookServiceException.BookBadId.class
                ,BookServiceException.BookAlreadyReturned.class
                ,ReaderServiceException.ReaderBadId.class})
        protected ResponseEntity<Object> handleNotFound(RuntimeException ex
                , WebRequest request) {
            String bodyOfResponse = "run time exception dude";
            return handleExceptionInternal(ex, bodyOfResponse,
                    new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        }

        @ExceptionHandler(value = { ReaderServiceException.AlreadyRented.class})
        protected ResponseEntity<Object> handleBadRequest(RuntimeException ex
                , WebRequest request) {
            String bodyOfResponse = "run time exception dude";
            return handleExceptionInternal(ex, bodyOfResponse,
                    new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }

}