package com.devthalys.inventorycontrolsystem.rest.errorsMessages;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public class ApiErrors {

    private List<String> errors;
    private LocalDateTime time;

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors(String errorMessage, LocalDateTime time){
        this.errors = Arrays.asList(errorMessage);
        this.time = time;
    }
}
