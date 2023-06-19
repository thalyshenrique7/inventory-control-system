package com.devthalys.inventorycontrolsystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {

    private String clientCpf;
    private String product;
    private Integer quantity;
    private float totalCost;
}
