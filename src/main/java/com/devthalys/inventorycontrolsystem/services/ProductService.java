package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.ProductModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    ProductModel findById(Long id);
    List<ProductModel> findByName(String name);
    ProductModel findByBarCode(Long barCode);
    boolean existsByName(String name);
    boolean existsByBarCode(Long barCode);
    void update(ProductModel product);
    void deleteById(Long id);
}
