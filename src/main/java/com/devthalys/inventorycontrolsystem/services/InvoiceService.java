package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.InvoiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceService {

    Page<InvoiceModel> findAll(Pageable pageable);
    InvoiceModel findByNumberInvoice(Long numberInvoice);
    boolean existsNumberInvoice(Long numberInvoice);
    void deleteByNumberInvoice(Long numberInvoice);
}
