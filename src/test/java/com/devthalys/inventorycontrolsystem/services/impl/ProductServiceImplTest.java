package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    private ProductModel product;

    private InventoryModel inventory;

    @Mock
    private Observable observable;

    @BeforeEach
    void setUp() {
        openMocks(this);

        product = new ProductModel();
        inventory = new InventoryModel();

        product.setId(1L);
        product.setName("Iphone");
        product.setBarCode(1200L);
        product.setQuantityMin(10);
        product.setBalance(100);
        product.setInventory(inventory);
        inventory.setProduct(product);
        inventory.setProductCategory(inventory.getProductCategory());
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
        assertEquals(product.getBalance(), response.getBalance());
        assertEquals(product.getInventory().getProductCategory(), response.getInventory().getProductCategory());
    }

    @Test
    void whenFindByIdThenThrowProductNotFoundException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(product.getId()));
    }

    @Test
    void whenFindByNameThenReturnSuccess() {
        List<ProductModel> products = Collections.singletonList(product);
        when(productRepository.findByNameIgnoreCase(anyString())).thenReturn(products);

        List<ProductModel> response = productService.findByNameIgnoreCase(product.getName());

        assertNotNull(response);
        assertEquals(products.size(), response.size());
        assertEquals(products.get(0).getId(), response.get(0).getId());
    }

    @Test
    void whenFindByNameThenThrowProductNotFoundException() {
        when(productRepository.findByNameIgnoreCase(anyString())).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> productService.findByNameIgnoreCase(product.getName()));
    }

    @Test
    void whenFindByBarCodeThenReturnSuccess() {
        when(productRepository.existsByBarCode(anyLong())).thenReturn(true);
        when(productRepository.findByBarCode(anyLong())).thenReturn(product);

        ProductModel response = productService.findByBarCode(product.getBarCode());

        assertNotNull(response);
        assertEquals(ProductModel.class, response.getClass());
        assertEquals(product.getBarCode(), response.getBarCode());
    }

    @Test
    void whenFindByBarCodeThenThrowProductNotFoundException() {
        when(productRepository.findByBarCode(anyLong())).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> productService.findByBarCode(product.getBarCode()));
    }

    @Test
    void whenSaveProductThenReturnSuccess() {
        when(productRepository.save(product)).thenReturn(product);

        ProductModel response = productService.save(product);

        assertNotNull(response);
        assertEquals(ProductModel.class, response.getClass());
        assertEquals(product.getId(), response.getId());
    }

    @Test
    void whenUpdateProductThenReturnSuccess() {
        ProductModel newProduct = new ProductModel();
        newProduct.setId(1L);
        newProduct.setName("Macbook");
        newProduct.setBarCode(10000L);
        newProduct.setBalance(200);
        newProduct.setQuantityMin(20);
        InventoryModel newInventory = new InventoryModel();
        newInventory.setProductCategory(product.getInventory().getProductCategory());
        newProduct.setInventory(newInventory);
        newInventory.setProduct(newProduct);

        when(productRepository.save(newProduct)).thenReturn(newProduct);
        productService.update(newProduct);
        verify(productRepository).save(newProduct);
        verify(observable).notifyStockChange(newProduct);
    }

    @Test
    void whenUpdateProductThenThrowProductNotFoundException(){
        ProductModel productNotFound = new ProductModel();
        productNotFound.setId(10L);

        when(productRepository.save(productNotFound)).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> productService.update(productNotFound));

        verify(productRepository).save(productNotFound);
    }

    @Test
    void whenExistsByBarCodeThenReturnSuccess() {
        when(productRepository.existsByBarCode(anyLong())).thenReturn(true);

        boolean exists = productService.existsByBarCode(product.getBarCode());

        assertTrue(exists);
    }

    @Test
    void whenExistsByBarCodeThenReturnException() {
        when(productRepository.existsByBarCode(anyLong())).thenReturn(false);

        boolean exists = productService.existsByBarCode(product.getBarCode());

        assertFalse(exists);
    }
}