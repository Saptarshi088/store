package com.saptarshi.store.controller;

import com.saptarshi.store.dto.UserDto;
import com.saptarshi.store.entities.User;
import com.saptarshi.store.mappers.UserMapper;
import com.saptarshi.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "name" , name = "sort") String sort){
        if(!Set.of("name","email").contains(sort)){
            sort = "name";
        }
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        return new ResponseEntity<>(user,HttpStatus.OK);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
