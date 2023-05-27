package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.dtos.FieldsListInventoryDto;
import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.exceptions.ProductAlreadyExistsException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.exceptions.ValueInvalidException;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.ProductRepository;
import com.devthalys.inventorycontrolsystem.repositories.InventoryRepository;
import com.devthalys.inventorycontrolsystem.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Observable observable;

    @Override
    public Optional<InventoryModel> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    public List<InventoryModel> findByProductName(String name){
        if(!inventoryRepository.existsByProductName(name)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema");
        }
        return inventoryRepository.findByProductName(name);
    }

    public List<InventoryModel> findByDateMovementBetween(LocalDateTime startDate, LocalDateTime endDate){
        List<InventoryModel> findDateMovement = inventoryRepository.findByMovementDateBetween(startDate, endDate);
        if(findDateMovement.isEmpty()){
            throw new ProductNotFoundException("Produto não cadastrado no sistema");
        }
        return inventoryRepository.findByMovementDateBetween(startDate, endDate);
    }

    public List<InventoryModel> findByMovementType(MovementType movementType){
        if(!inventoryRepository.existsByMovementType(movementType)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema");
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

    @Override
    public InventoryModel findByProduct(ProductModel product) {
        return inventoryRepository.findByProduct(product);
    }

    @Override
    public void updateRegister(InventoryModel inventory){
        checkBalance(inventory);
//        checkMovementType(stock);
        checkExistsProduct(inventory);
        existsReleaseProduct(inventory);
        existsInitialBalance(inventory);
        generateDocument(inventory);

        inventoryRepository.save(inventory);
        observable.notifyStockChange(inventory);
    }

    public void saveMovement(InventoryModel stock){
        checkBalance(stock);
        existsReleaseProduct(stock);
        existsInitialBalance(stock);
        generateDocument(stock);
        stock.setMovementDate(LocalDateTime.now());

        observable.notifyStockChange(stock);
        inventoryRepository.save(stock);
    }

//    public void checkMovementType(StockModel stock){
//        MovementType movementType = stock.getMovementType();
//
//        if(movementType == MovementType.SALDO_INICIAL ||
//                movementType == MovementType.AJUSTE_ENTRADA ||
//                movementType == MovementType.AJUSTE_SAIDA){
//        }
//    }

    public void existsInitialBalance(InventoryModel stock){
        MovementType movementType = stock.getMovementType();
        ProductModel product = stock.getProduct();

        if(movementType == MovementType.SALDO_INICIAL &&
                inventoryRepository.existsByProductIdAndMovementType(product.getId(), MovementType.SALDO_INICIAL)){
            throw new ProductAlreadyExistsException("Produto já possui lançamento Saldo Inicial");
        }
    }

    public void existsReleaseProduct(InventoryModel stock){
        MovementType movementType = stock.getMovementType();
        ProductModel product = stock.getProduct();

        if(!productRepository.existsById(product.getId()) && movementType == MovementType.AJUSTE_ENTRADA){
            throw new ProductNotFoundException("Não existem lançamentos para esse produto.");
        } else if(!productRepository.existsById(product.getId()) && movementType == MovementType.AJUSTE_SAIDA){
            throw new ProductNotFoundException("Não existem lançamentos para esse produto.");
        }
    }

    public void checkBalance(InventoryModel stock){
        int newBalance = stock.getProduct().getInitialBalance() + stock.getBalance();

        if(newBalance < 0){
            throw new ValueInvalidException("Saldo Total não pode ser menor que Quantidade Mínima.");
        }
        stock.setBalance(newBalance);

        if(newBalance < stock.getProduct().getQuantityMin()){
            stock.setSituation("Inferior ao mínimo.");
        } else {
            stock.setSituation("OK");
        }
    }

    public void checkExistsProduct(InventoryModel inventory){
        if(!(productRepository.existsById(inventory.getProduct().getId()))){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
    }

    public void generateDocument(InventoryModel stock){
        if(!(stock.getMovementType() == MovementType.ENTRADA
                || stock.getMovementType() == MovementType.SAIDA)){
            stock.setDocument(null);
        }
        stock.setDocument(stock.getDocument());
    }

    public List<FieldsListInventoryDto> listByProductStock() {
        List<InventoryModel> stockList = inventoryRepository.findByOrderByMovementDate();
        List<FieldsListInventoryDto> stockDtoList = new ArrayList<>();

        for (InventoryModel stock : stockList) {
            FieldsListInventoryDto stockDto = new FieldsListInventoryDto();
            stockDto.setMovementDate(stock.getMovementDate());
            stockDto.setProduct(stock.getProduct());
            stockDto.setMovementType(stock.getMovementType());
            stockDto.setDocument(stock.getDocument());
            stockDto.setReason(stock.getReason());
            stockDto.setBalance(stock.getBalance());
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
    public boolean existsByProduct(ProductModel product) {
        return inventoryRepository.existsByProduct(product);
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
