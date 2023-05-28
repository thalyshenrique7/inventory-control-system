package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.dtos.UpdateCredentialsDto;
import com.devthalys.inventorycontrolsystem.exceptions.UserException;
import com.devthalys.inventorycontrolsystem.models.UserModel;
import com.devthalys.inventorycontrolsystem.services.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/users")
@Api(value = "Inventory Control System")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/")
    @ApiOperation(value = "Users list")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<UserModel>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping(value = "/{login}")
    @ApiOperation(value = "Search user by login")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<UserModel> findByLogin(@PathVariable String login){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByLogin(login));
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "Save User")
    @ApiResponses({ @ApiResponse(code = 201, message = "User register success"),
                    @ApiResponse(code = 404, message = "User not found")})
    public ResponseEntity<UserModel> save(@RequestBody UserModel user){
        if(userService.existsByLogin(user.getLogin())){
            throw new UserException("Usuário já possui cadastro no sistema.");
        }
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @DeleteMapping(value = "/delete/{login}")
    @ApiOperation(value = "Delete User")
    @ApiResponses({ @ApiResponse(code = 204, message = "User deleted success"),
                    @ApiResponse(code = 404, message = "User not found")})
    public ResponseEntity<Object> delete(@PathVariable String login){
        UserModel user = userService.findByLogin(login);

        if(user == null){
            throw new UserException("Usuário não encontrado.");
        }

        userService.delete(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuário deletado com sucesso.");
    }

    @PutMapping(value = "/update/{login}")
    @ApiOperation(value = "Update User")
    @ApiResponses({ @ApiResponse(code = 204, message = "User updated success"),
                    @ApiResponse(code = 404, message = "User not found")})
    public ResponseEntity<Object> update(@PathVariable String login, @RequestBody @Valid UpdateCredentialsDto userDto){
        UserModel user = userService.findByLogin(login);

        if(user == null){
            throw new UserException("Usuário não encontrado.");
        }

        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);
        user.setManager(userDto.isManager());

        userService.save(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuário atualizado com sucesso");
    }
}


