package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.exceptions.InventoryException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.ReportModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.InventoryRepository;
import com.devthalys.inventorycontrolsystem.repositories.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.devthalys.inventorycontrolsystem.enums.MovementType.SALDO_INICIAL;
import static com.devthalys.inventorycontrolsystem.enums.ProductCategory.CELULARES;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.Assert.*;

class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ReportRepository reportRepository;
    private InventoryModel inventory;
    private ProductModel product;
    private ReportModel report;
    @Mock
    private Observable observable;

    @BeforeEach
    void setUp() {
        openMocks(this);

        inventory = new InventoryModel();
        product = new ProductModel();
        report = new ReportModel();
        observable = new Observable();

        product.setId(1L);
        product.setName("Iphone");
        product.setBarCode(10000L);
        product.setQuantityMin(100);
        product.setBalance(inventory.getQuantity());
        product.setPrice(1999);
        product.setInventory(inventory);

        inventory.setId(2L);
        inventory.setReason("Compras");
        inventory.setDocument("Compras de produtos");
        inventory.setSituation("Saldo superior a quantidade mínima");
        inventory.setQuantity(1000);
        inventory.setMovementDate(LocalDateTime.now());
        inventory.setProduct(product);
        inventory.setMovementType(SALDO_INICIAL);
        inventory.setProductCategory(CELULARES);

        report.setId(3L);
        report.setReason(inventory.getReason());
        report.setDocument(inventory.getDocument());
        report.setSituation(inventory.getSituation());
        report.setQuantity(inventory.getQuantity());
        report.setMovementDate(inventory.getMovementDate());
        report.setProductName(product.getName());
        report.setBarCode(product.getBarCode());
        report.setQuantityMin(product.getQuantityMin());
        report.setBalance(product.getBalance());
        report.setPriceUnit(product.getPrice());
        report.setMovementType(inventory.getMovementType());
        report.setProductCategory(inventory.getProductCategory());
    }

    @Test
    void findProductByBalanceLessThanQuantityMin() {
    }

    @Test
    void findProductByBalanceGreaterThanQuantityMin() {
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(inventory));

        InventoryModel response = inventoryService.findById(inventory.getId());

        assertNotNull(response);
        assertEquals(inventory.getId(), response.getId());
        assertEquals(InventoryModel.class, response.getClass());
    }

    @Test
    public void whenFindByIdThenThrowException(){
        when(inventoryRepository.findById(anyLong())).thenThrow(InventoryException.class);

        assertThrows(InventoryException.class, () -> inventoryService.findById(inventory.getId()));
    }

    @Test
    void whenFindByProductNameThenReturnSucess() {
        when(inventoryRepository.existsByProductName(anyString())).thenReturn(true);
        when(inventoryRepository.findByProductName(anyString())).thenReturn(List.of(inventory));

        List<InventoryModel> response = inventoryService.findByProductName(inventory.getProduct().getName());

        assertNotNull(response);
        assertEquals(inventory.getProduct().getName(), response.get(0).getProduct().getName());
    }

    @Test
    public void whenFindByProductNameThenThrowException(){
        when(inventoryRepository.findByProductName(anyString())).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> inventoryService.findByProductName(inventory.getProduct().getName()));
    }

//    @Test
    void whenFindByDateMovementBetweenThenReturnSuccess() {
        when(inventoryRepository.findByMovementDateBetween(any(), any())).thenReturn(List.of(inventory), List.of(inventory));
    }

    @Test
    void whenFindByMovementTypeThenReturnSuccess() {
        when(inventoryRepository.existsByMovementType(any())).thenReturn(true);
        when(inventoryRepository.findByMovementType(any())).thenReturn(List.of(inventory));

        List<InventoryModel> response = inventoryService.findByMovementType(inventory.getMovementType());

        assertNotNull(response);
        assertEquals(inventory.getMovementType(), response.get(0).getMovementType());
    }

    @Test
    public void whenFindByMovementTypeThenThrowException(){
        when(inventoryRepository.findByMovementType(any())).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> inventoryService.findByMovementType(inventory.getMovementType()));
    }

    @Test
    void whenFindByOrderByProductThenReturnSuccess() {
        when(inventoryRepository.findByOrderByProduct()).thenReturn(List.of(inventory));

        List<InventoryModel> response = inventoryService.findByOrderByProduct();

        assertNotNull(response);
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getProduct().getName(), inventory.getProduct().getName());
    }

    @Test
    public void whenFindByOrderByProductThenThrowException(){
        when(inventoryRepository.findByOrderByProduct()).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> inventoryService.findByOrderByProduct());
    }

    @Test
    void whenFindByOrderByDateMovementThenSuccess() {
        when(inventoryRepository.findByOrderByMovementDate()).thenReturn(List.of(inventory));

        List<InventoryModel> response = inventoryService.findByOrderByDateMovement();

        assertNotNull(response);
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getProduct().getName(), inventory.getProduct().getName());
    }

    @Test
    void whenListByProductInventoryThenReturnSuccess() {
        when(inventoryRepository.findByOrderByProduct()).thenReturn(List.of(inventory));

        List<InventoryModel> response = inventoryService.findByOrderByProduct();

        assertNotNull(response);
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getProduct().getName(), inventory.getProduct().getName());
    }

    @Test
    void whenSaveInventoryThenReturnSuccess() {
        when(inventoryRepository.save(any())).thenReturn(inventory);
        when(reportRepository.save(any())).thenReturn(report);

        inventoryService.save(inventory);
        observable.notifyStockChange(inventory);
        reportRepository.save(report);

        verify(inventoryRepository, times(1)).save(any());
    }

    @Test
    void whenUpdateInventoryThenReturnSuccess() {
    }

    @Test
    void whenDeleteInventoryThenReturnSuccess() {
    }

    @Test
    void existsInitialBalance() {
    }

    @Test
    void checkBalance() {
    }

    @Test
    void checkMovementBeforeSave() {
    }

    @Test
    void checkQuantityStock() {
    }

    @Test
    void generateDocument() {
    }

    @Test
    void generateInvoice() {
    }

    @Test
    void calculatePriceTotal() {
    }

    @Test
    void saveBin() {
    }

    @Test
    void saveReport() {
    }

    @Test
    void existsByProductName() {
    }

    @Test
    void existsByDateMovementBetween() {
    }

    @Test
    void existsByMovementType() {
    }

    @Test
    void findByProductIdAndMovementType() {
    }

    @Test
    void findByProductId() {
    }

    @Test
    void existsByProductId() {
    }

    @Test
    void existsByProductIdAndMovementType() {
    }
}