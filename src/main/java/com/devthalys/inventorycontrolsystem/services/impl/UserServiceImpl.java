package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.models.UserModel;
import com.devthalys.inventorycontrolsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserModel findByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public UserModel save(UserModel user){
        return userRepository.save(user);
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
