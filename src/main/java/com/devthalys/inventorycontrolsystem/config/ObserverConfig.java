package com.devthalys.inventorycontrolsystem.config;

import com.devthalys.inventorycontrolsystem.observers.ChangeObserver;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {
    private final Observable stockObservable;
    private final ChangeObserver stockChangeObserver;

    public ObserverConfig(Observable stockObservable, ChangeObserver stockChangeObserver) {
        this.stockObservable = stockObservable;
        this.stockChangeObserver = stockChangeObserver;

        stockObservable.addStockObserver(stockChangeObserver);
    }
}
