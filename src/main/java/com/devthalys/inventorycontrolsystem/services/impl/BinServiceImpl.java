package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.models.BinModel;
import com.devthalys.inventorycontrolsystem.repositories.BinRepository;
import com.devthalys.inventorycontrolsystem.services.BinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinServiceImpl implements BinService {

    @Autowired
    private BinRepository binRepository;

    @Override
    public BinModel findById(Long id) {
        return binRepository.findById(id)
                .map( bin -> {
                    bin.getId();
                    return bin;
                }).orElseThrow(() -> new RuntimeException("Item na lixeira não foi encontrado"));
    }

    @Override
    public List<BinModel> findAll() {
        return binRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        binRepository.deleteById(id);
    }
}
