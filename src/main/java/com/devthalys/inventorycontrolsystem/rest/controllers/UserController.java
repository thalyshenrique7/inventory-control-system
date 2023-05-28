package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.models.UserModel;
import com.devthalys.inventorycontrolsystem.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/save")
    public ResponseEntity<UserModel> save(@RequestBody UserModel user){
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }
}
