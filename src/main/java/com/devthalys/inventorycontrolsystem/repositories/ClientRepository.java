package com.devthalys.inventorycontrolsystem.repositories;

import com.devthalys.inventorycontrolsystem.models.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<ClientModel, Long> {

    @Query("select c from ClientModel c left join fetch c.shoppingList where c.id = :id")
    ClientModel findClientFetchShopping(@Param("id") Long id);
    ClientModel findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
