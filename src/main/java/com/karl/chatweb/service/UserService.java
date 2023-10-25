package com.karl.chatweb.service;

import com.karl.chatweb.dto.UserDto;
import com.karl.chatweb.model.User;
import com.karl.chatweb.exception.UserAlreadyExistsException;
import com.karl.chatweb.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    public User registerUser(UserDto userDto) throws UserAlreadyExistsException {
        if(userNameExists(userDto.username)){
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = User.builder()
                .username(userDto.username)
                .password(passwordEncoder.encode(userDto.password))
                .color("#" + StringUtils.upperCase(userDto.color))
                .role("USER")
                .build();

        return repository.save(user);
    }

    private boolean userNameExists(String username){
        return repository.findByUsername(username) != null;
    }
}
