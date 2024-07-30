package ru.practicum.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.ErrorMessage;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundException(NotFoundException e) {
        return ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage notFoundException(HttpMessageNotReadableException e) {
        return ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage invalidParametersException(InvalidParametersException e) {
        return ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage conflictException(ConflictException e) {
        return ErrorMessage.builder()
                .status(HttpStatus.CONFLICT)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
