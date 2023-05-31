package com.devthalys.inventorycontrolsystem.repositories;

import com.devthalys.inventorycontrolsystem.models.InvoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceModel, Long> {


}
