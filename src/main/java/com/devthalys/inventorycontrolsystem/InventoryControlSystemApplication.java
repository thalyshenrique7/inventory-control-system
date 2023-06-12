package com.devthalys.inventorycontrolsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InventoryControlSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryControlSystemApplication.class, args);
    }

}
