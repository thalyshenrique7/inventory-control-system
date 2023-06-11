package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.exceptions.UserException;
import com.devthalys.inventorycontrolsystem.models.UserModel;
import com.devthalys.inventorycontrolsystem.observers.Observable;
import com.devthalys.inventorycontrolsystem.repositories.UserRepository;
import com.devthalys.inventorycontrolsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Observable observable;

    public List<UserModel> findAll(){
        return userRepository.findAll();
    }

    @Override
    public UserModel findById(Long id) {
        return userRepository.findById(id)
                .map( user -> {
                    user.getId();
                    return user;
                }).orElseThrow(() -> new UserException("Usuário não encontrado."));
    }

    public UserModel findByLogin(String login){
        return userRepository.findByLogin(login);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public UserModel save(UserModel user){
        observable.notifyStockChange(user);
        return userRepository.save(user);
    }

    public void delete(UserModel user){
        userRepository.delete(user);
    }

    @Override
    public void update(UserModel user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByLogin(username);

        if(user == null){
            throw new RuntimeException("Usuário não encontrado.");
        }

        String[] roles = user.isManager() ? new String[]{"MANAGER"} : new String[]{"OPERATOR"};

        return User
                .builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
