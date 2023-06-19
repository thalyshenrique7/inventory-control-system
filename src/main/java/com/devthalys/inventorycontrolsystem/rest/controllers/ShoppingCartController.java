package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.dtos.ShoppingCartDto;
import com.devthalys.inventorycontrolsystem.exceptions.ClientException;
import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.models.ShoppingCartModel;
import com.devthalys.inventorycontrolsystem.services.impl.ClientServiceImpl;
import com.devthalys.inventorycontrolsystem.services.impl.ShoppingCartServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shopping")
@Api(value = "Inventory Control System")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping(value = "/{orderNumber}")
    @ApiOperation(value = "Find by order number")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<ShoppingCartModel> findByOrderNumber(@PathVariable Long orderNumber){
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartService.findByOrderNumber(orderNumber));
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "Add product into client")
    @ApiResponse(code = 200, message = "201")
    public ResponseEntity<Object> addProduct(@RequestBody ShoppingCartDto shoppingCartDto){
        shoppingCartService.addProduct(shoppingCartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added success.");
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
