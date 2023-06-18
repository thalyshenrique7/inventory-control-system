package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.models.ShoppingCartModel;
import org.springframework.stereotype.Service;

@Service
public interface ShoppingCartService {
    ShoppingCartModel findByOrderNumber(Long orderNumber);
    void addProduct(ClientModel client, Long barCode, Integer quantity);
    void removeProduct(Long shoppingCart);
    void buyProducts(ClientModel client);
}
