package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.exceptions.ClientException;
import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.repositories.ClientRepository;
import com.devthalys.inventorycontrolsystem.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientModel> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public ClientModel findByCpf(String cpf) {
        if(clientRepository.existsByCpf(cpf)){
            throw new ClientException("Client already exists in system.");
        }
        return clientRepository.findByCpf(cpf);
    }

    @Override
    public ClientModel findClientFetchShopping(Long id) {
        return clientRepository.findClientFetchShopping(id);
    }

    @Override
    public void save(ClientModel client) {
        clientRepository.save(client);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return clientRepository.existsByCpf(cpf);
    }
}
