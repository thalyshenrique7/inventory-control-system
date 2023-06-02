package com.devthalys.inventorycontrolsystem.models;

import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reports")
public class ReportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String reason;

    @Column
    private String document;

    @Column
    private String situation;

    @Column
    private int quantity;

    @Column
    private LocalDateTime movementDate;

    @Column
    private String productName;

    @Column
    private Long barCode;

    @Column
    private int quantityMin;

    @Column
    private int balance;

    @Column
    private float priceUnit;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
}
