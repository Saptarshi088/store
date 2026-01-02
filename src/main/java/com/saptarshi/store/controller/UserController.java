package com.saptarshi.store.controller;

import com.saptarshi.store.dto.UserDto;
import com.saptarshi.store.entities.User;
import com.saptarshi.store.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public Iterable<UserDto> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(user->new UserDto(user.getId(),user.getName(),user.getEmail()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        return new ResponseEntity<>(user,HttpStatus.OK);
        return ResponseEntity.ok(new UserDto(user.getId(),user.getName(),user.getEmail()));
    }
}
