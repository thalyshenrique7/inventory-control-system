package com.devthalys.inventorycontrolsystem.dtos;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementUpdateDto {

    private Long inventoryId;
    private MovementType movementType;
    private int balance;
    private LocalDateTime movementDate;
    private String reason;
    private String document;
    private String situation;
    private ProductCategory productCategory;
}
