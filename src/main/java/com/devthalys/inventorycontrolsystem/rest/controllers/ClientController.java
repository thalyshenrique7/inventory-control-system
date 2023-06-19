package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.exceptions.ClientException;
import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.services.impl.ClientServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clients")
@Api(value = "Inventory Control System")
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping(value = "/")
    @ApiOperation(value = "Find all clients")
    @ApiResponses( {@ApiResponse(code = 404, message = "Clients do not registered in system."),
                    @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<List<ClientModel>> findAll(){
        List<ClientModel> clientList = clientService.findAll();
        if(clientList.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClientException("Clients do not registered in system."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAll());
    }

    @GetMapping(value = "/{cpf}")
    @ApiResponses( {@ApiResponse(code = 404, message = "Clients do not registered in system."),
                    @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<ClientModel> findByCpf(@PathVariable String cpf){
        ClientModel client = clientService.findByCpf(cpf);
        if(client == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClientException("Client do not registered in system."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @GetMapping(value = "/shopping/{id}")
    @ApiResponses( {@ApiResponse(code = 404, message = "Clients do not registered in system."),
                    @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<ClientModel> findClientFetchShopping(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findClientFetchShopping(id));
    }

    @PostMapping(value = "/save")
    @ApiResponse(code = 201, message = "Client registered success.")
    public ResponseEntity<Object> save(@RequestBody ClientModel client){
        clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body("Client registered success.");
    }
}
