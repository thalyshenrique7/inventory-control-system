package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface InventoryService {
    List<InventoryModel> findProductByBalanceLessThanQuantityMin();
    List<InventoryModel> findProductByBalanceGreaterThanQuantityMin();
    List<InventoryModel> findByProductName(String name);
    List<InventoryModel> findByDateMovementBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<InventoryModel> findByMovementType(MovementType movementType);
    List<InventoryModel> findByOrderByProduct();
    List<InventoryModel> findByOrderByDateMovement();
    InventoryModel findById(Long id);
    InventoryModel findByProductId(ProductModel productModel);
    InventoryModel findByProductIdAndMovementType(ProductModel productModel, MovementType movementType);
    boolean existsByProductName(String name);
    boolean existsByDateMovementBetween(LocalDateTime startDate, LocalDateTime endDate);
    boolean existsByMovementType(MovementType movementType);
    boolean existsByProductId(Long productId);
    boolean existsByProductIdAndMovementType(Long productId, MovementType movementType);
    void update(InventoryModel inventory);
    void delete(InventoryModel inventory);
}
