package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.LoginUserRequest;
import com.example.demo.model.WebResponse;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(
            value = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<LoginResponse> login(@RequestBody LoginUserRequest request){
        LoginResponse loginResponse = authenticationService.login(request);
        return WebResponse.<LoginResponse>builder()
                .data(loginResponse)
                .build();
    }


    @Parameter(name = "user", hidden = true)
    @PostMapping(
            value = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout(User user){
        authenticationService.logout(user);
        return WebResponse.<String>builder()
                .data("logout success")
                .build();
    }
}
