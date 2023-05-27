package com.devthalys.inventorycontrolsystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDto {

    private String name;
    private Integer quantityMin;
}
