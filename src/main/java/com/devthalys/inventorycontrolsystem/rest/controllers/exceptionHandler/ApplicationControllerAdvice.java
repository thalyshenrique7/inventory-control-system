package com.devthalys.inventorycontrolsystem.rest.controllers.exceptionHandler;

import com.devthalys.inventorycontrolsystem.exceptions.*;
import com.devthalys.inventorycontrolsystem.rest.errorsMessages.ApiErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

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
    @ResponseStatus(CONFLICT)
    public ApiErrors handleProductAlreadyExistsException(ProductAlreadyExistsException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handleProductNotFoundException(ProductNotFoundException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiErrors handleUnauthorizedAccessException(UserException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(ValueInvalidException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handleBalanceInvalidException(ValueInvalidException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(SaveMovementException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleSaveMovementException(SaveMovementException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(InventoryException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleInventoryException(InventoryException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(InvoiceException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleInvoiceException(InvoiceException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(BinException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleBinException(BinException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleClientException(ClientException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }

    @ExceptionHandler(ShoppingCartException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleShoppingCartException(ShoppingCartException e){
        String errorMessage = e.getMessage();
        LocalDateTime timeNow = LocalDateTime.now();
        return new ApiErrors(errorMessage, timeNow);
    }
}
