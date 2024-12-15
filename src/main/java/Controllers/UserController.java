package Controllers;

import Dto.LoginDto;
import Dto.UserDto;
import Services.JwtUtil;
import Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import tables.UserRole;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Admin creates users with roles
    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto, @RequestParam("password") String password, @RequestParam("role") String role) {
        return userService.register(userDto, password, UserRole.valueOf(role.toUpperCase()));
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        if (token == null) {
            throw new RuntimeException("Invalid Credentials");
        }
        return token;
    }

    @GetMapping("/profile")
    public UserDto getProfile(HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        return userService.getUserProfile(userId);
    }
}