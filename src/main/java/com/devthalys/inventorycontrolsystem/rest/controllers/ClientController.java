package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.exceptions.ClientException;
import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.services.impl.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping(value = "/")
    public ResponseEntity<List<ClientModel>> findAll(){
        List<ClientModel> clientList = clientService.findAll();
        if(clientList.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClientException("Clients do not registered in system."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAll());
    }

    @GetMapping(value = "/{cpf}")
    public ResponseEntity<ClientModel> findByCpf(@PathVariable String cpf){
        ClientModel client = clientService.findByCpf(cpf);
        if(client == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClientException("Client do not registered in system."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<Object> save(@RequestBody ClientModel client){
        clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body("Client registered success.");
    }
}
