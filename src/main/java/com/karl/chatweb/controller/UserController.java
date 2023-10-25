package com.karl.chatweb.controller;


import com.karl.chatweb.dto.UserDto;
import com.karl.chatweb.model.User;
import com.karl.chatweb.exception.UserAlreadyExistsException;
import com.karl.chatweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDto dto){
        try{
            User user = userService.registerUser(dto);
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return ResponseEntity.ok(map);
        }catch(UserAlreadyExistsException e){
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("error", "User already exists");
            return ResponseEntity.status(400).body(map);
        }
    }
}
