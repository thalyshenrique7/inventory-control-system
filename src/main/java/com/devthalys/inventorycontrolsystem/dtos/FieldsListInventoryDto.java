package com.devthalys.inventorycontrolsystem.dtos;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldsListInventoryDto {

    private LocalDateTime movementDate;
    private ProductModel product;
    private MovementType movementType;
    private String document;
    private String reason;
    private String situation;
}
