package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.exceptions.ValueInvalidException;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.ProductRepository;
import com.devthalys.inventorycontrolsystem.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Observable observable;

    @Override
    public ProductModel findById(Long id) {
        return productRepository.findById(id)
                .map( productId -> {
                    productId.getId();
                    return productId;
                }).orElseThrow(() -> new ProductNotFoundException("Produto não cadastrado no sistema."));
    }

    public List<ProductModel> findByNameIgnoreCase(String name){
        List<ProductModel> products = productRepository.findByNameIgnoreCase(name);
        if(products == null){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return products;
    }

    public ProductModel findByBarCode(Long barCode){
        if(!productRepository.existsByBarCode(barCode)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return productRepository.findByBarCode(barCode);
    }

    public ProductModel save(ProductModel product){
        if(product.getQuantityMin() < 0){
            throw new ValueInvalidException("Quantidade mínima não pode ser menor que 0.");
        }

        product.setName(product.getName());
        product.setBarCode(product.getBarCode());
        product.setQuantityMin(product.getQuantityMin());
        product.setInitialBalance(product.getInitialBalance());

        if(product.getInitialBalance() > 0) {
            InventoryModel inventory = new InventoryModel();
            inventory.setMovementType(MovementType.SALDO_INICIAL);
            inventory.setProductCategory(product.getInventory().getProductCategory());
            inventory.setBalance(product.getInitialBalance());
            inventory.setMovementDate(LocalDateTime.now());
            product.setInventory(inventory);
            inventory.setProduct(product);
        }
        observable.notifyStockChange(product);
        return productRepository.save(product);
    }

    @Override
    public boolean existsByBarCode(Long barCode) {
        return productRepository.existsByBarCode(barCode);
    }

    @Override
    public void update(ProductModel product) {
        productRepository.save(product);
        observable.notifyStockChange(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
