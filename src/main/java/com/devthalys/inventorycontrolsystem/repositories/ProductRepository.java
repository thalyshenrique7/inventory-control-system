package com.devthalys.inventorycontrolsystem.repositories;

import com.devthalys.inventorycontrolsystem.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Optional<ProductModel> findById(Long id);
    List<ProductModel> findByNameIgnoreCase(String name);
    ProductModel findByName(String productName);
    ProductModel findByBarCode(Long barCode);
    boolean existsByBarCode(Long barCode);
    void deleteById(Long id);
}
