package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.exceptions.ClientException;
import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.services.impl.ClientServiceImpl;
import com.devthalys.inventorycontrolsystem.services.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/shopping")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private ClientServiceImpl clientService;

    @PostMapping(value = "/add")
    public ResponseEntity<Object> addProduct(ClientModel client, Long barCode, Integer quantity){
        shoppingCartService.addProduct(client, barCode, quantity);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product added success.");
    }

    @PostMapping(value = "/buy")
    public ResponseEntity<Object> buyProduct(ClientModel client){
        ClientModel findClient = clientService.findByCpf(client.getCpf());

        if(findClient == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClientException("Client not found."));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product added success.");
    }
}
