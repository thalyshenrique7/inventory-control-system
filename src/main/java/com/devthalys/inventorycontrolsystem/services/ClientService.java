package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.ClientModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {
    List<ClientModel> findAll();
    ClientModel findByCpf(String cpf);
    void save(ClientModel client);
    boolean existsByCpf(String cpf);
}
