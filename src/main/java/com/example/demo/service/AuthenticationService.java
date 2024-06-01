package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.LoginUserRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public LoginResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(nextWeek());
        userRepository.save(user);

        return LoginResponse.builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();
    }

    private Long nextWeek() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
    }

    @Transactional
    public void logout(User user) {
        user.setTokenExpiredAt(System.currentTimeMillis());

        userRepository.save(user);
    }
}
