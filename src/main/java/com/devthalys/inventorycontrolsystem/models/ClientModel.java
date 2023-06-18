package com.devthalys.inventorycontrolsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class ClientModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String secondName;

    @CPF
    private String cpf;

    @Column
    private float wallet;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ShoppingCartModel> shoppingList;
}
