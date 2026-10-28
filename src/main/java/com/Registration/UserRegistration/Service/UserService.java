package com.Registration.UserRegistration.Service;


import com.Registration.UserRegistration.DTO.JwtAuthDto;
import com.Registration.UserRegistration.DataBase.User;
import com.Registration.UserRegistration.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder  passwordEncoder;
    private JWTService jwtService;
    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public void createUser(User user) {
        if(!user.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")){
            throw new IllegalArgumentException("Не правильно набран email");
        }
        if(!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$\n")){
            throw new IllegalArgumentException("пароль не надежный");
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()){}
        passwordEncoder.encode(user.getPassword());
        userRepository.save(user);
    }
    public JwtAuthDto login(String email, String clearPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("пользователь не найден"));
        if(!passwordEncoder.matches(clearPassword, user.getPassword())) {
            throw new IllegalArgumentException("неверный пароль");
        }
        if(!passwordEncoder.matches(clearPassword, user.getPassword())) {
            userRepository.findByEmail(user.getEmail()).get();
        }
        return jwtService.generateAuthJwtToken(user.getEmail());
    }
}
