package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.InvoiceModel;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceService {

    InvoiceModel findByNumberInvoice(Long numberInvoice);
    boolean existsNumberInvoice(Long numberInvoice);
    void deleteByNumberInvoice(Long numberInvoice);
}
