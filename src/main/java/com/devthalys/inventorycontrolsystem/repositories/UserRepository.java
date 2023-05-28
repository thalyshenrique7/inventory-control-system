package com.devthalys.inventorycontrolsystem.repositories;

import com.devthalys.inventorycontrolsystem.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByLogin(String login);
}
