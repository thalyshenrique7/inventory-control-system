package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    private ProductModel product = new ProductModel();

    private InventoryModel inventory = new InventoryModel();

    private Observable observable = new Observable();

    @BeforeEach
    void setUp() {
        openMocks(this);

        product.setId(1L);
        product.setName("Alexander McQueen");
        product.setBarCode(101010L);
        product.setQuantityMin(10);
        product.setInitialBalance(10);
        inventory.setProduct(product);
        product.setInventory(inventory);
        inventory.setProductCategory(product.getInventory().getProductCategory());
    }

    @Test
    void whenFindByIdThenReturnProductSuccess() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
        
        ProductModel response = productService.findById(product.getId());

        assertNotNull(response);
        assertEquals(ProductModel.class, response.getClass());
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getBarCode(), response.getBarCode());
        assertEquals(product.getQuantityMin(), response.getQuantityMin());
        assertEquals(product.getInitialBalance(), response.getInitialBalance());
        assertEquals(product.getInventory().getProductCategory(), response.getInventory().getProductCategory());
    }

    @Test
    void findByName() {
    }

    @Test
    void findByBarCode() {
    }

    @Test
    void whenSaveProductThenReturnSuccess() {
        when(productRepository.save(product)).thenReturn(product);

        ProductModel response = productService.save(product);
        observable.notifyStockChange(product);

        assertNotNull(response);
        assertEquals(ProductModel.class, response.getClass());
        assertEquals(product.getId(), response.getId());
    }

    @Test
    void update() {
    }

    @Test
    void existsByName() {
    }

    @Test
    void existsByBarCode() {
    }
}