package com.devthalys.inventorycontrolsystem.observers;

import com.devthalys.inventorycontrolsystem.interfaces.Observer;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.models.InvoiceModel;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class ChangeObserver implements Observer {
    @Override
    public void onInventoryChange(Object observer) {
        if (observer instanceof ProductModel) {
            handleProductChange((ProductModel) observer);
            calculateInventory(((ProductModel) observer).getInventory().getProduct().getInventory());
        } else if (observer instanceof InventoryModel) {
            handleInventoryChange((InventoryModel) observer);
            calculateInventory(((InventoryModel) observer).getProduct().getInventory());
        } else if(observer instanceof UserModel){
            handleUserChange((UserModel) observer);
        } else if(observer instanceof InvoiceModel){
            handleInvoiceChange((InvoiceModel) observer);
        }
    }

    private void handleProductChange(ProductModel product) {
        System.out.println("Product Observer - Product Change: "
                + "\nProduct Name: " + product.getName()
                + "\nBarcode: " + product.getBarCode()
                + "\nQuantity Minimum: " + product.getQuantityMin()
                + "\nBalance: " + product.getBalance()
                + "\nPrice: " + product.getPrice()
                + "\nProduct Category: " + product.getInventory().getProductCategory()
                + "\n");
    }

    private void handleInventoryChange(InventoryModel inventory) {
        System.out.println("Inventory Observer - Inventory Change: "
                + "\nProduct Name: " + inventory.getProduct().getName()
                + "\nBarcode: " + inventory.getProduct().getBarCode()
                + "\nQuantity Minimum: " + inventory.getProduct().getQuantityMin()
                + "\nBalance: " + inventory.getProduct().getBalance()
                + "\nPrice Unit: " + inventory.getProduct().getPrice()
                + "\nMovement Type: " + inventory.getMovementType()
                + "\nProduct Category: " + inventory.getProductCategory()
                + "\nDate Movement: " + inventory.getMovementDate()
                + "\n");
    }

    public void handleUserChange(UserModel user){
        System.out.println("User Observer: "
        + "\nLogin: " + user.getLogin()
        + "\n" + verifyRole()
        + "\n");
    }

    public void handleInvoiceChange(InvoiceModel invoice){
        System.out.println("Invoice Observer - Invoice deleted: "
        + "\nNumber Invoice: " + invoice.getNumberInvoice()
        + "\nProduct ID: " + invoice.getProductId()
        + "\nProduct Name: " + invoice.getProductName()
        + "\nQuantity: " + invoice.getQuantity()
        + "\nProduct Unit: " + invoice.getPriceUnit()
        + "\nPrice Total: " + invoice.getPriceTotal()
        + "\nSale Date: " + invoice.getSaleDate());
    }

    public void calculateInventory(InventoryModel inventory) {
        int balance = inventory.getProduct().getBalance();
        int product = inventory.getProduct().getQuantityMin();

        if (balance < product) {
            System.out.println("Alerta: Quantidades em estoque estão abaixo da quantidade mínima!");
        }
    }

    public String verifyRole(){
        UserModel user = new UserModel();
        return user.isManager() ? "Role: Operator" : "Role: Manager";
    }
}