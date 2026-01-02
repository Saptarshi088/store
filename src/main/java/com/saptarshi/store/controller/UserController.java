package com.saptarshi.store.controller;

import com.saptarshi.store.entities.User;
import com.saptarshi.store.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        return new ResponseEntity<>(user,HttpStatus.OK);
        return ResponseEntity.ok(user);
    }
}
