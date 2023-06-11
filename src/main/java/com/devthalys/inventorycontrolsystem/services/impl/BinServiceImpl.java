package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.exceptions.BinException;
import com.devthalys.inventorycontrolsystem.models.BinModel;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.repositories.BinRepository;
import com.devthalys.inventorycontrolsystem.repositories.InventoryRepository;
import com.devthalys.inventorycontrolsystem.services.BinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinServiceImpl implements BinService {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

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
    public void restoreBin(BinModel bin) {
        BinModel binId = binRepository.findById(bin.getId())
                .map(findBin -> {
                    findBin.getId();
                    return findBin;
                }).orElseThrow(() -> new BinException("Relatório não foi encontrado ou não existe na lixeira."));

        if(binId != null){
            InventoryModel inventory = new InventoryModel();
            inventory.setProduct(new ProductModel());
            inventory.getProduct().setName(binId.getName());
            inventory.getProduct().setBarCode(binId.getBarCode());
            inventory.getProduct().setQuantityMin(binId.getQuantityMin());

            inventory.setReason(binId.getReason());
            inventory.setDocument(binId.getDocument());
            inventory.setSituation(binId.getSituation());
            inventory.setQuantity(binId.getBalance());
            inventory.setMovementDate(binId.getMovementDate());
            inventory.setMovementType(binId.getMovementType());
            inventory.setProductCategory(binId.getProductCategory());

            inventoryRepository.save(inventory);
            binRepository.delete(binId);
        }
    }

    @Override
    public void deleteById(Long id) {
        binRepository.deleteById(id);
    }
}
