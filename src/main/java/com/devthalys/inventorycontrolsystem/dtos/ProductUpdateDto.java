package com.devthalys.inventorycontrolsystem.dtos;

import com.devthalys.inventorycontrolsystem.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDto {

    private String name;
    private int quantityMin;
    private float priceUnit;
    private ProductCategory productCategory;
}
