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
        } else if(observer instanceof InventoryModel){
            handleInventoryChange((InventoryModel) observer);
        }
    }

    private void handleProductChange(ProductModel product) {
        System.out.println("Product Observer - Product Change: "
                + "\nProduct Name: " + product.getName()
                + "\nBarcode: " + product.getBarCode()
                + "\nQuantity Minimum: " + product.getQuantityMin()
                + "\nInitial Balance: " + product.getInitialBalance()
                + "\nProduct Category: " + product.getInventory().getProductCategory()
                + "\n");
    }

    private void handleInventoryChange(InventoryModel inventory) {
        System.out.println("Inventory Observer - Inventory Change: "
                + "\nProduct Name: " + inventory.getProduct().getName()
                + "\nBarcode: " + inventory.getProduct().getBarCode()
                + "\nQuantity Minimum: " + inventory.getProduct().getQuantityMin()
                + "\nInitial Balance: " + inventory.getProduct().getInitialBalance()
                + "\nMovement Type: " + inventory.getMovementType()
                + "\nProduct Category: " + inventory.getProductCategory()
                + "\nDate Movement: " + inventory.getMovementDate()
                + "\n");
    }
}