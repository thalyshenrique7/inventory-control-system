package com.devthalys.inventorycontrolsystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shopping_cart")
public class ShoppingCartModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderNumber;

    @Column
    private int quantity;

    @Column
    private float totalCost;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonManagedReference
    private ClientModel client;

    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private ProductModel product;
}
