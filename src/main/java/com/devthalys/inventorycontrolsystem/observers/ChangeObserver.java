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

    private void handleInventoryChange(InventoryModel stock) {
        System.out.println("Inventory Observer - Inventory Change: "
                + "\nStock Id: " + stock.getId()
                + "\nProduct Name: " + stock.getProduct().getName()
                + "\nMovement Type: " + stock.getMovementType()
                + "\nDate Movement: " + stock.getMovementDate()
                + "\n");
    }
}