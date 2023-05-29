package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.dtos.FieldsListInventoryDto;
import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.exceptions.ProductAlreadyExistsException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.exceptions.SaveMovementException;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.BinModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.InventoryRepository;
import com.devthalys.inventorycontrolsystem.repositories.BinRepository;
import com.devthalys.inventorycontrolsystem.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

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

    public void save(InventoryModel inventory){
        checkMovementBeforeSave(inventory);
        checkBalance(inventory);
        generateDocument(inventory);

        inventory.setMovementDate(LocalDateTime.now());

        observable.notifyStockChange(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void update(InventoryModel inventory){
        existsInitialBalance(inventory);
        checkBalance(inventory);
        generateDocument(inventory);

        inventory.getProduct().setBarCode(inventory.getProduct().getBarCode());
        inventory.setMovementDate(LocalDateTime.now());

        inventoryRepository.save(inventory);
        observable.notifyStockChange(inventory);
    }

    @Override
    public void delete(InventoryModel inventory) {
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
        bin.setProductCategory(inventory.getProductCategory());
        binInventory.save(bin);

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
        } else if(movementType == MovementType.SAIDA){
            int newBalance = currentBalance - inventory.getQuantity();
            inventory.getProduct().setBalance(newBalance);
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

    public void generateDocument(InventoryModel inventory){
        if(!(inventory.getMovementType() == MovementType.ENTRADA
                || inventory.getMovementType() == MovementType.SAIDA)){
            inventory.setDocument(null);
        }
        inventory.setDocument(inventory.getDocument());
    }

    public List<FieldsListInventoryDto> listByProductInventory() {
        List<InventoryModel> stockList = inventoryRepository.findByOrderByMovementDate();
        List<FieldsListInventoryDto> stockDtoList = new ArrayList<>();

        for (InventoryModel stock : stockList) {
            FieldsListInventoryDto stockDto = new FieldsListInventoryDto();
            stockDto.setMovementDate(stock.getMovementDate());
            stockDto.setProduct(stock.getProduct());
            stockDto.setMovementType(stock.getMovementType());
            stockDto.setDocument(stock.getDocument());
            stockDto.setReason(stock.getReason());
            stockDto.setSituation(stock.getSituation());

            stockDtoList.add(stockDto);
        }
        return stockDtoList;
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
