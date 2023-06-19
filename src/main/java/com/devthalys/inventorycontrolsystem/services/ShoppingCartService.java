package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.dtos.ShoppingCartDto;
import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.models.ShoppingCartModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService {
    ShoppingCartModel findByOrderNumber(Long orderNumber);
    void addProduct(ShoppingCartDto shoppingCartDto);
    void removeProduct(Long shoppingCart);
    void buyProducts(ClientModel client);
}
