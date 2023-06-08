package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.BinModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BinService {

    BinModel findById(Long id);
    List<BinModel> findAll();
    void restoreBin(BinModel bin);
    void deleteById(Long id);
}
