package com.saptarshi.store.controller;

import com.saptarshi.store.dto.JwtResponse;
import com.saptarshi.store.dto.UserDto;
import com.saptarshi.store.dto.UserLogInRequest;
import com.saptarshi.store.mappers.UserMapper;
import com.saptarshi.store.repositories.UserRepository;
import com.saptarshi.store.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> userLogIn(
            @RequestBody UserLogInRequest request,
            HttpServletResponse response
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        var user = userRepository.getUserByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setSecure(true);
        cookie.setMaxAge(604800);

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/validate")
    public Boolean validateToken(@RequestHeader(name = "Authorization") String token) {
        return jwtService.validateToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElse(null);

        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userMapper.toDto(user));


//        if (authentication == null ||
//                !authentication.isAuthenticated() ||
//                authentication.getPrincipal().equals("anonymousUser")) {
//            return ResponseEntity.status(401).build();
//        }
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String email = userDetails.getUsername();
//        System.out.println(email);
//
//        return userRepository.findByEmail(email)
//                .map(user -> ResponseEntity.ok(userMapper.toDto(user)))
//                .orElseGet(() -> ResponseEntity.notFound().build());


    }

}
