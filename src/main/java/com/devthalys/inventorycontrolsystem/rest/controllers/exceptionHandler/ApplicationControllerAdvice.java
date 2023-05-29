package com.devthalys.inventorycontrolsystem.rest.controllers.exceptionHandler;

import com.devthalys.inventorycontrolsystem.exceptions.*;
import com.devthalys.inventorycontrolsystem.rest.errorsMessages.ApiErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    // Error List
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException e){
        List<String> errors = e.getBindingResult().getAllErrors()
                .stream()
                .map( error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiErrors(errors);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleProductAlreadyExistsException(ProductAlreadyExistsException e){
        String errorMessage = e.getMessage();
        return new ApiErrors(errorMessage);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleProductNotFoundException(ProductNotFoundException e){
        String errorMessage = e.getMessage();
        return new ApiErrors(errorMessage);
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleUnauthorizedAccessException(UserException e){
        String errorMessage = e.getMessage();
        return new ApiErrors(errorMessage);
    }

    @ExceptionHandler(ValueInvalidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleBalanceInvalidException(ValueInvalidException e){
        String errorMessage = e.getMessage();
        return new ApiErrors(errorMessage);
    }

    @ExceptionHandler(SaveMovementException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleSaveMovementException(SaveMovementException e){
        String errorMessage = e.getMessage();
        return new ApiErrors(errorMessage);
    }

    @ExceptionHandler(InventoryException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleInventoryException(InventoryException e){
        String errorMessage = e.getMessage();
        return new ApiErrors(errorMessage);
    }
}
