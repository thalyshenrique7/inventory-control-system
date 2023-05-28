package com.devthalys.inventorycontrolsystem.observers;

import com.devthalys.inventorycontrolsystem.interfaces.Observer;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import org.springframework.stereotype.Component;

@Component
public class ChangeObserver implements Observer {
    @Override
    public void onInventoryChange(Object observer) {
        if(observer instanceof ProductModel){
            handleProductChange((ProductModel) observer);
            calculateInventory(((ProductModel) observer).getInventory().getProduct().getInventory());
        } else if(observer instanceof InventoryModel){
            handleInventoryChange((InventoryModel) observer);
            calculateInventory(((InventoryModel) observer).getProduct().getInventory());
        }
    }

    private void handleProductChange(ProductModel product) {
        System.out.println("Product Observer - Product Change: "
                + "\nProduct Name: " + product.getName()
                + "\nBarcode: " + product.getBarCode()
                + "\nQuantity Minimum: " + product.getQuantityMin()
                + "\nBalance: " + product.getBalance()
                + "\nProduct Category: " + product.getInventory().getProductCategory()
                + "\n");
    }

    private void handleInventoryChange(InventoryModel inventory) {
        System.out.println("Inventory Observer - Inventory Change: "
                + "\nProduct Name: " + inventory.getProduct().getName()
                + "\nBarcode: " + inventory.getProduct().getBarCode()
                + "\nQuantity Minimum: " + inventory.getProduct().getQuantityMin()
                + "\nBalance: " + inventory.getProduct().getBalance()
                + "\nMovement Type: " + inventory.getMovementType()
                + "\nProduct Category: " + inventory.getProductCategory()
                + "\nDate Movement: " + inventory.getMovementDate()
                + "\n");
    }

    public void calculateInventory(InventoryModel inventory){
        int balance = inventory.getProduct().getBalance();
        int product = inventory.getProduct().getQuantityMin();

        if(balance < product){
            System.out.println("Alerta: Quantidades em estoque estão abaixo da quantidade mínima!");
        }
    }
}