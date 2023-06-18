package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.ProductModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    ProductModel findById(Long id);
    List<ProductModel> findByNameIgnoreCase(String name);
    ProductModel findByName(String productName);
    ProductModel findByBarCode(Long barCode);
    boolean existsByBarCode(Long barCode);
    void deleteById(Long id);
    void update(ProductModel product);
}
