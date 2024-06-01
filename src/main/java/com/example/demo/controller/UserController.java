package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.RegisterUserRequest;
import com.example.demo.model.UpdateUserPasswordRequest;
import com.example.demo.model.UserResponse;
import com.example.demo.model.WebResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(
            value = "/api/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> register(@RequestBody RegisterUserRequest request){
        UserResponse userResponse = userService.register(request);
        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }


    @Parameter(name = "user", hidden = true)
    @GetMapping(
            value = "/api/user/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user){
        UserResponse userResponse = userService.getUser(user);
        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }


    @Parameter(name = "user", hidden = true)
    @PutMapping(
            value = "/api/user/password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> updatePassword(User user, @RequestBody UpdateUserPasswordRequest request){
        UserResponse userResponse = userService.updateUserPassword(user, request);
        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }
}
