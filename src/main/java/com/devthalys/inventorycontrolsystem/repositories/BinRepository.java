package com.devthalys.inventorycontrolsystem.repositories;

import com.devthalys.inventorycontrolsystem.models.BinModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinRepository extends JpaRepository<BinModel, Long> {
    List<BinModel> findByDeleted(boolean deleted);
}
