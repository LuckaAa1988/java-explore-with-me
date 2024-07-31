package ru.practicum.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.model.DateException;
import ru.practicum.exception.model.ErrorMessage;


import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(DateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage dateException(DateException e) {
        return ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Wrong dates.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
