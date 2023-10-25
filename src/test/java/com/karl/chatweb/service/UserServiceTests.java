package com.karl.chatweb.service;

import static org.assertj.core.api.Assertions.*;

import com.karl.chatweb.dto.UserDto;
import com.karl.chatweb.model.User;
import com.karl.chatweb.exception.UserAlreadyExistsException;
import com.karl.chatweb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_RegisterUser_ReturnsUser() throws UserAlreadyExistsException {
        UserDto dto = UserDto.builder()
                .username("username")
                .password("password")
                .color("ff0000")
                .build();

        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("password")
                .username("username")
                .badges(new ArrayList<>())
                .build();

        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        User savedUser = userService.registerUser(dto);

        assertThat(savedUser).isNotNull();
    }

    @Test
    public void UserService_RegisterUser_ThrowsUserAlreadyExistsException() throws UserAlreadyExistsException {
        UserDto dto = UserDto.builder()
                .username("username")
                .password("password")
                .color("ff0000")
                .build();

        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("password")
                .username("username")
                .badges(new ArrayList<>())
                .build();

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        assertThatThrownBy(() -> {
            User savedUser = userService.registerUser(dto);
        }).isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("Username already exists");


    }
}
