package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.dtos.FieldsListInventoryDto;
import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.exceptions.ProductAlreadyExistsException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.exceptions.SaveMovementException;
import com.devthalys.inventorycontrolsystem.exceptions.ValueInvalidException;
import com.devthalys.inventorycontrolsystem.models.*;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.BinRepository;
import com.devthalys.inventorycontrolsystem.repositories.InventoryRepository;
import com.devthalys.inventorycontrolsystem.repositories.InvoiceRepository;
import com.devthalys.inventorycontrolsystem.repositories.ReportRepository;
import com.devthalys.inventorycontrolsystem.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private BinRepository binInventory;

    @Autowired
    private Observable observable;

    @Override
    public List<InventoryModel> findProductByBalanceLessThanQuantityMin() {
        return inventoryRepository.findProductByBalanceLessThanQuantityMin();
    }

    @Override
    public List<InventoryModel> findProductByBalanceGreaterThanQuantityMin() {
        return inventoryRepository.findProductByBalanceGreaterThanQuantityMin();
    }

    @Override
    public InventoryModel findById(Long id) {
        return inventoryRepository.findById(id)
                .map( inventory -> {
                    inventory.getId();
                    return inventory;
                }).orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
    }

    public List<InventoryModel> findByProductName(String name){
        if(!inventoryRepository.existsByProductName(name)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return inventoryRepository.findByProductName(name);
    }

    public List<InventoryModel> findByDateMovementBetween(LocalDateTime startDate, LocalDateTime endDate){
        List<InventoryModel> findDateMovement = inventoryRepository.findByMovementDateBetween(startDate, endDate);
        if(findDateMovement.isEmpty()){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return inventoryRepository.findByMovementDateBetween(startDate, endDate);
    }

    public List<InventoryModel> findByMovementType(MovementType movementType){
        if(!inventoryRepository.existsByMovementType(movementType)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return inventoryRepository.findByMovementType(movementType);
    }

    @Override
    public List<InventoryModel> findByOrderByProduct() {
        return inventoryRepository.findByOrderByProduct();
    }

    @Override
    public List<InventoryModel> findByOrderByDateMovement() {
        return inventoryRepository.findByOrderByMovementDate();
    }

    public List<FieldsListInventoryDto> listByProductInventory() {
        List<InventoryModel> inventoryList = inventoryRepository.findByOrderByMovementDate();

        return inventoryList.stream()
                .map(inventory -> {
                    FieldsListInventoryDto inventoryDto = new FieldsListInventoryDto();
                    inventoryDto.setMovementDate(inventory.getMovementDate());
                    inventoryDto.setProduct(inventory.getProduct());
                    inventoryDto.setMovementType(inventory.getMovementType());
                    inventoryDto.setDocument(inventory.getDocument());
                    inventoryDto.setReason(inventory.getReason());
                    inventoryDto.setSituation(inventory.getSituation());
                    return inventoryDto;
                }).collect(Collectors.toList());
    }

    public String verifyBestSeller(){
        Map<String, Integer> quantitiesByProduct = inventoryRepository.findByMovementType(MovementType.SAIDA).stream()
                .collect(Collectors.groupingBy(inventory -> inventory.getProduct().getName(),
                        Collectors.summingInt(InventoryModel::getQuantity)));

        Optional<Map.Entry<String, Integer>> mostSoldProductEntry = quantitiesByProduct.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));

        if (mostSoldProductEntry.isPresent()) {
            Map.Entry<String, Integer> entry = mostSoldProductEntry.get();
            String mostSoldProduct = entry.getKey();
            int quantitySold = entry.getValue();
            return "O produto mais vendido é: " + mostSoldProduct + ", com quantidade vendida de: " + quantitySold + " unidades.";
        } else {
            return "Nenhum produto vendido encontrado.";
        }
    }

    public void save(InventoryModel inventory){
        checkMovementBeforeSave(inventory);
        checkBalance(inventory);
        generateDocument(inventory);

        inventory.setQuantity(inventory.getProduct().getBalance());
        inventory.setMovementDate(LocalDateTime.now());

        inventoryRepository.save(inventory);
        observable.notifyStockChange(inventory);
        saveReport(inventory);
    }

    @Override
    public void update(InventoryModel inventory){
        existsInitialBalance(inventory);
        checkBalance(inventory);
        checkQuantityStock(inventory);
        generateDocument(inventory);

        inventory.getProduct().setBarCode(inventory.getProduct().getBarCode());
        inventory.setMovementDate(LocalDateTime.now());

        inventoryRepository.save(inventory);
        observable.notifyStockChange(inventory);
        saveReport(inventory);
    }

    @Override
    public void delete(InventoryModel inventory) {
        saveBin(inventory);
        inventoryRepository.delete(inventory);
    }

    public void existsInitialBalance(InventoryModel inventory){
        MovementType movementType = inventory.getMovementType();
        ProductModel product = inventory.getProduct();

        if(movementType == MovementType.SALDO_INICIAL &&
                inventoryRepository.existsByProductIdAndMovementType(product.getId(), MovementType.SALDO_INICIAL)){
            throw new ProductAlreadyExistsException("Produto já possui lançamento Saldo Inicial");
        }
    }

    public void checkBalance(InventoryModel inventory){
        MovementType movementType = inventory.getMovementType();
        int currentBalance = inventory.getProduct().getBalance();

        if(movementType == MovementType.ENTRADA) {
            int newBalance = currentBalance + inventory.getQuantity();
            inventory.getProduct().setBalance(newBalance);

            generateInvoice(inventory);

        } else if(movementType == MovementType.SAIDA){
            int newBalance = currentBalance - inventory.getQuantity();
            inventory.getProduct().setBalance(newBalance);

            generateInvoice(inventory);
        }

        if(currentBalance < inventory.getProduct().getQuantityMin()){
            inventory.setSituation("Saldo inferior ao mínimo.");
        } else {
            inventory.setSituation("Saldo superior ao mínimo.");
        }
    }

    public void checkMovementBeforeSave(InventoryModel inventory){
        MovementType movementType = inventory.getMovementType();

        if(!(movementType == MovementType.SALDO_INICIAL)){
            throw new SaveMovementException("Apenas o movimento Saldo Inicial é permitido para novos produtos.");
        }
    }

    public void checkQuantityStock(InventoryModel inventory){
        MovementType movementType = inventory.getMovementType();

        if(movementType == MovementType.SAIDA){
            if(inventory.getQuantity() > inventory.getProduct().getBalance()){
                throw new ValueInvalidException("A quantidade informada não está disponível em estoque.");
            }
        }
    }



    public void generateDocument(InventoryModel inventory){
        if(!(inventory.getMovementType() == MovementType.ENTRADA
                || inventory.getMovementType() == MovementType.SAIDA)){
            inventory.setDocument(null);
        }
        inventory.setDocument(inventory.getDocument());
    }

    public void generateInvoice(InventoryModel inventory){
        InvoiceModel invoice = new InvoiceModel();
        invoice.setProductId(inventory.getProduct().getId());
        invoice.setProductName(inventory.getProduct().getName());
        invoice.setQuantity(inventory.getQuantity());
        invoice.setPriceUnit(inventory.getProduct().getPrice());
        invoice.setReason(inventory.getReason());
        invoice.setSaleDate(LocalDateTime.now());

        calculatePriceTotal(invoice, inventory);
        invoiceRepository.save(invoice);
    }

    public void calculatePriceTotal(InvoiceModel invoice, InventoryModel inventory){
        float calculate = inventory.getQuantity() * inventory.getProduct().getPrice();
        invoice.setPriceTotal(calculate);
    }

    public void saveBin(InventoryModel inventory){
        BinModel bin = new BinModel();
        bin.setName(inventory.getProduct().getName());
        bin.setBarCode(inventory.getProduct().getBarCode());
        bin.setQuantityMin(inventory.getProduct().getQuantityMin());
        bin.setBalance(inventory.getProduct().getBalance());
        bin.setReason(inventory.getReason());
        bin.setDocument(inventory.getDocument());
        bin.setSituation(inventory.getSituation());
        bin.setDeletionDate(LocalDateTime.now());
        bin.setDeleted(true);
        bin.setMovementDate(inventory.getMovementDate());
        bin.setMovementType(inventory.getMovementType());
        bin.setProductCategory(inventory.getProductCategory());
        binInventory.save(bin);
    }

    public void saveReport(InventoryModel inventory){
        ReportModel report = new ReportModel();
        report.setReason(inventory.getReason());
        report.setDocument(inventory.getDocument());
        report.setSituation(inventory.getSituation());
        report.setQuantity(inventory.getQuantity());
        report.setMovementDate(inventory.getMovementDate());
        report.setProductName(inventory.getProduct().getName());
        report.setBarCode(inventory.getProduct().getBarCode());
        report.setQuantityMin(inventory.getProduct().getQuantityMin());
        report.setBalance(inventory.getProduct().getBalance());
        report.setPriceUnit(inventory.getProduct().getPrice());
        report.setMovementType(inventory.getMovementType());
        report.setProductCategory(inventory.getProductCategory());
        reportRepository.save(report);
    }

    public boolean existsByProductName(String name){
        return inventoryRepository.existsByProductName(name);
    }

    public boolean existsByDateMovementBetween(LocalDateTime startDate, LocalDateTime endDate){
        return inventoryRepository.existsByMovementDateBetween(startDate, endDate);
    }

    public boolean existsByMovementType(MovementType movementType){
        return inventoryRepository.existsByMovementType(movementType);
    }

    @Override
    public InventoryModel findByProductIdAndMovementType(ProductModel product, MovementType movementType) {
        return inventoryRepository.findByProductIdAndMovementType(product.getId(), product.getInventory().getMovementType());
    }

    @Override
    public InventoryModel findByProductId(ProductModel product) {
        return inventoryRepository.findByProductId(product.getId());
    }

    @Override
    public boolean existsByProductId(Long productId) {
        return inventoryRepository.existsById(productId);
    }

    @Override
    public boolean existsByProductIdAndMovementType(Long productId, MovementType movementType) {
        return inventoryRepository.existsByProductIdAndMovementType(productId, movementType);
    }
}
