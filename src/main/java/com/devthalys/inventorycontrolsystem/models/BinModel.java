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
@Table(name = "bin")
public class BinModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private Long barCode;

    @Column
    private int quantityMin;

    @Column
    private int balance;

    @Column
    private String reason;

    @Column
    private String document;

    @Column
    private String situation;

    @Column
    private LocalDateTime movementDate;

    @Column
    private LocalDateTime deletionDate;

    @Column
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
}
