package Services;


import Dto.LoginDto;
import Dto.UserDto;
import Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tables.User;
import tables.UserRole;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDto register(UserDto userDto, String rawPassword, UserRole role) {
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .role(role)
                .password(passwordEncoder.encode(rawPassword))
                .build();
        user = userRepository.save(user);
        userDto.setId(user.getId());
        userDto.setRole(user.getRole().name());
        return userDto;
    }

    public String login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername()).orElse(null);
        if (user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return JwtUtil.generateToken(user);
        }
        return null;
    }

    public UserDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        return dto;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}