package com.devthalys.inventorycontrolsystem.observers;

import com.devthalys.inventorycontrolsystem.interfaces.Observer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addStockObserver(Observer observer){
        observers.add(observer);
    }

    public void removeStockObserver(Observer observer){
        observers.remove(observer);
    }

    public Object notifyStockChange(Object observer){
        for(Observer stockObserver : observers){
            stockObserver.onInventoryChange(observer);
        }
        return observer;
    }
}
