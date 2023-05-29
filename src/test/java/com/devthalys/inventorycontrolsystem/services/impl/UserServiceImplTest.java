package com.devthalys.inventorycontrolsystem.services.impl;

import com.devthalys.inventorycontrolsystem.exceptions.UserException;
import com.devthalys.inventorycontrolsystem.models.UserModel;
import com.devthalys.inventorycontrolsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private UserModel user;

    @BeforeEach
    void setUp() {
        openMocks(this);

        user = new UserModel();
        user.setId(1L);
        user.setLogin("Gerente");
        user.setPassword("123");
        user.setManager(true);
    }

    @Test
    void whenFindAllThenReturnSuccess() {
        List<UserModel> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        List<UserModel> response = userService.findAll();

        assertNotNull(response);
        assertEquals(users.size(), response.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void whenFindAllThenThrowUserException() {
        when(userRepository.findAll()).thenThrow(UserException.class);

        assertThrows(UserException.class, () -> userService.findAll());
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        UserModel response = userService.findById(user.getId());

        assertNotNull(response);
        assertEquals(UserModel.class, response.getClass());
        assertEquals(user.getId(), response.getId());
    }

    @Test
    void whenFindByIdThenThrowUserException() {
        when(userRepository.findById(anyLong())).thenThrow(UserException.class);

        assertThrows(UserException.class, () -> userService.findById(user.getId()));
    }

    @Test
    void whenFindByLoginThenReturnSuccess() {
        when(userRepository.findByLogin(anyString())).thenReturn(user);

        UserModel response = userService.findByLogin(user.getLogin());

        assertNotNull(response);
        assertEquals(UserModel.class, response.getClass());
        assertEquals(user.getLogin(), response.getLogin());
    }

    @Test
    void whenFindByLoginThenThrowUserException() {
        when(userRepository.findByLogin(anyString())).thenThrow(UserException.class);

        assertThrows(UserException.class, () -> userService.findByLogin(user.getLogin()));
    }

    @Test
    void whenExistsByLoginThenReturnSuccess() {
        when(userRepository.existsByLogin(anyString())).thenReturn(true);

        boolean exists = userService.existsByLogin(user.getLogin());

        assertTrue(exists);
    }

    @Test
    void whenExistsByLoginThenThrowUserException() {
        when(userRepository.existsByLogin(anyString())).thenReturn(false);

        boolean nonExists = userService.existsByLogin(user.getLogin());

        assertFalse(nonExists);
    }

    @Test
    void whenSaveUserThenReturnSuccess() {
        when(userRepository.save(any())).thenReturn(user);

        UserModel response = userService.save(user);

        assertNotNull(response);
        assertEquals(UserModel.class, response.getClass());
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getLogin(), response.getLogin());
        assertEquals(user.getPassword(), response.getPassword());
        assertEquals(user.isManager(), response.isManager());
    }

    @Test
    void whenDeleteUserThenReturnSuccess() {
        userRepository.delete(user);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void whenUpdateUserThenReturnSuccess() {
        UserModel newUser = new UserModel();
        newUser.setId(1L);
        newUser.setPassword("456");
        newUser.setManager(false);

        when(userRepository.save(newUser)).thenReturn(newUser);
        userService.update(newUser);
        verify(userRepository).save(newUser);
    }

    @Test
    void whenLoadUserByUsernameThenReturnSuccess() {
        when(userRepository.findByLogin(user.getLogin())).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getLogin());

        assertNotNull(userDetails);
        assertEquals(user.getLogin(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_MANAGER", userDetails.getAuthorities().iterator().next().getAuthority());
        verify(userRepository, times(1)).findByLogin(user.getLogin());
    }
}