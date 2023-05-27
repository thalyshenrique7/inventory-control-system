package com.devthalys.inventorycontrolsystem.repositories;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {
    InventoryModel findByProductIdAndMovementType(Long productId, MovementType movementType);
    InventoryModel findByProductId(Long productId);
    InventoryModel findByProduct(ProductModel product);
    List<InventoryModel> findByProductName(String name);
    List<InventoryModel> findByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<InventoryModel> findByMovementType(MovementType movementType);
    List<InventoryModel> findByOrderByProduct();
    List<InventoryModel> findByOrderByMovementDate();
    boolean existsByProductName(String name);
    boolean existsByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    boolean existsByMovementType(MovementType movementType);
    boolean existsByProduct(ProductModel product);
    boolean existsByProductIdAndMovementType(Long productId, MovementType movementType);
}
