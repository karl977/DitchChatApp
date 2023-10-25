package com.karl.chatweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @GetMapping("/api/not_logged_in")
    public ResponseEntity<Object> notLoggedIn(){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Log in required");
        return ResponseEntity.status(401).body(map);
    }

    @RequestMapping("/api/login_success")
    public ResponseEntity<Object> loginSuccessful(){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Login successful");
        return ResponseEntity.status(200).body(map);
    }

    @RequestMapping("/api/logout_success")
    public ResponseEntity<Map<String, Object>> logoutSuccess(){
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return ResponseEntity.ok().body(map);
    }

    @RequestMapping("api/user/logged_in")
    public ResponseEntity<Map<String, Object>> isLoggedIn(HttpServletRequest request){
        SecurityContext sc = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if(sc.getAuthentication() != null &&
                sc.getAuthentication().isAuthenticated() &&
                !(sc.getAuthentication() instanceof AnonymousAuthenticationToken)){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "User authenticated");
            return ResponseEntity.ok().body(map);
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("message", "User not authenticated");
            return ResponseEntity.status(401).body(map);
        }
    }
}
