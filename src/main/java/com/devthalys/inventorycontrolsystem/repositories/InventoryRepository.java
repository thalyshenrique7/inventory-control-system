package com.devthalys.inventorycontrolsystem.repositories;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {

    @Query(" SELECT i FROM InventoryModel i WHERE i.product.balance < i.product.quantityMin ")
    List<InventoryModel> findProductByBalanceLessThanQuantityMin();

    @Query("SELECT i FROM InventoryModel i WHERE i.product.balance > i.product.quantityMin ")
    List<InventoryModel> findProductByBalanceGreaterThanQuantityMin();

    InventoryModel findByProductIdAndMovementType(Long productId, MovementType movementType);
    InventoryModel findByProductId(Long productId);
    List<InventoryModel> findByProductName(String name);
    List<InventoryModel> findByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<InventoryModel> findByMovementType(MovementType movementType);
    List<InventoryModel> findByOrderByProduct();
    List<InventoryModel> findByOrderByMovementDate();
    boolean existsByProductName(String name);
    boolean existsByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    boolean existsByMovementType(MovementType movementType);
    boolean existsByProductIdAndMovementType(Long productId, MovementType movementType);
}
