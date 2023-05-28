package com.devthalys.inventorycontrolsystem.services;

import com.devthalys.inventorycontrolsystem.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<UserModel> findAll();
    UserModel findById(Long id);
    UserModel findByLogin(String login);
    void delete(UserModel user);
    void update(UserModel user);
    boolean existsByLogin(String login);

}
