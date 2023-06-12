package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.exceptions.InvoiceException;
import com.devthalys.inventorycontrolsystem.models.InvoiceModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.InvoiceRepository;
import com.devthalys.inventorycontrolsystem.repositories.ProductRepository;
import com.devthalys.inventorycontrolsystem.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Observable observable;

    @Override
    public Page<InvoiceModel> findAll(Pageable pageable) {
        return invoiceRepository.findAll(pageable);
    }

    @Override
    public InvoiceModel findByNumberInvoice(Long numberInvoice) {
        return invoiceRepository.findById(numberInvoice)
                .map( invoice -> {
                    invoice.getNumberInvoice();
                    return invoice;
                }).orElseThrow(() -> new InvoiceException("Nota Fiscal não foi encontrada no sistema."));
    }

    @Override
    public boolean existsNumberInvoice(Long numberInvoice) {
        return invoiceRepository.existsById(numberInvoice);
    }

    @Override
    public void deleteByNumberInvoice(Long numberInvoice) {
        Optional<InvoiceModel> invoice = invoiceRepository.findById(numberInvoice);
        if (invoice.isPresent()) {
            long productId = invoice.get().getProductId();
            int quantity = invoice.get().getQuantity();

            Optional<ProductModel> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                ProductModel product = optionalProduct.get();
                product.setBalance(product.getBalance() + quantity);
                productRepository.save(product);
            } else {
                throw new InvoiceException("Nota Fiscal não encontrada no sistema.");
            }
            invoiceRepository.deleteByNumberInvoice(numberInvoice);
            observable.notifyStockChange(invoice);
        }
    }
}
