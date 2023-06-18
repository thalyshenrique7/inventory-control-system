package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.exceptions.ShoppingCartException;
import com.devthalys.inventorycontrolsystem.models.ClientModel;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.ShoppingCartModel;
import com.devthalys.inventorycontrolsystem.repositories.InventoryRepository;
import com.devthalys.inventorycontrolsystem.repositories.ProductRepository;
import com.devthalys.inventorycontrolsystem.repositories.ShoppingCartRepository;
import com.devthalys.inventorycontrolsystem.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public ShoppingCartModel findByOrderNumber(Long orderNumber) {
        return shoppingCartRepository.findById(orderNumber)
                .map(order -> {
                    order.getOrderNumber();
                    return order;
                }).orElseThrow(() -> new ShoppingCartException("Order number not found."));
    }

    @Override
    public void addProduct(ClientModel client, Long barCode, Integer quantity) {
        ProductModel product = productRepository.findByBarCode(barCode);

        if(product == null){
            throw new ProductNotFoundException("Product not found.");
        }

        var shoppingCart = new ShoppingCartModel();
        shoppingCart.setClient(client);
        shoppingCart.setProduct(product);
        shoppingCart.setQuantity(quantity);

        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void removeProduct(Long shoppingCartId) {
        shoppingCartRepository.deleteById(shoppingCartId);
    }

    @Override
    public void buyProducts(ClientModel client) {
        List<ShoppingCartModel> shoppingCartList = client.getShoppingList();

        if(shoppingCartList.isEmpty()){
            throw new ShoppingCartException("Shopping cart is empty.");
        }

        for(ShoppingCartModel shopping : shoppingCartList){
            ProductModel product = shopping.getProduct();
            int quantity = shopping.getQuantity();

            if(client.getWallet() >= product.getPrice() * quantity && product.getBalance() >= quantity){
                client.setWallet(client.getWallet() - product.getPrice() * quantity);
                product.setBalance(product.getBalance() - quantity);

                var inventory = new InventoryModel();
                inventory.setReason("Compra realizada através do cliente: " + client.getCpf());
                inventory.setDocument("Documento de compra do cliente: " + client.getCpf());
                inventory.setMovementDate(LocalDateTime.now());
                inventory.setProduct(product);
                inventory.setMovementType(MovementType.SAIDA);
                inventory.setProductCategory(product.getInventory().getProductCategory());
                shopping.setTotalCost(product.getPrice() * quantity);

                inventoryRepository.save(inventory);

                shopping.setQuantity(0);
            }
        }
    }
}
