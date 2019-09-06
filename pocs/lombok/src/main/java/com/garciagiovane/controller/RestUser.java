package com.garciagiovane.controller;

import com.garciagiovane.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class RestUser {

    @PostMapping("")
    public User saveUser(@RequestBody User user){
        return user;
    }

    @GetMapping("")
    public ResponseEntity getBuilderUser(){
        User user = User.builder().name("giovane").age(23).email("giovane@giovane.com").build();
        Map<String, String> userInformation = Map.ofEntries(
                Map.entry("user", user.toString()),
                Map.entry("hashcode", ""+user.hashCode()));

        return ResponseEntity.ok(userInformation);
    }
}
