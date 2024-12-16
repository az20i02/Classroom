package com.example.demo.controllers;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserDto;
import com.example.demo.services.JwtUtil;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.tables.UserRole;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto,
                            @RequestParam("password") String password,
                            @RequestParam("role") String role) {
        return userService.register(userDto, password, UserRole.valueOf(role.toUpperCase()));
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        if (token == null) {


            throw new RuntimeException("Invalid Credentials : "+ loginDto.getUsername());
        }
        return token;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public UserDto getProfile(HttpServletRequest request) {
        Long userId = jwtUtil.extractUserId(request);
        return userService.getUserProfile(userId);
    }
}
