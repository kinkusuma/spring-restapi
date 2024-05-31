package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.model.RegisterUserRequest;
import com.example.demo.model.UpdateUserPasswordRequest;
import com.example.demo.model.UserResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        validationService.validate(request);

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
                });

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);

        return toResponse(user);
    }

    public UserResponse getUser(User user) {
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUserPassword(User user, UpdateUserPasswordRequest request) {
        if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis());
        userRepository.save(user);

        return toResponse(user);
    }
}
