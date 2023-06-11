package com.devthalys.inventorycontrolsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class InvoiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 5)
    private Long numberInvoice;

    @Column
    private Long productId;

    @Column
    private String productName;

    @Column
    private int quantity;

    @Column
    private float priceUnit;

    @Column
    private float priceTotal;

    @Column
    private String reason;

    @Column
    private LocalDateTime saleDate;
}
