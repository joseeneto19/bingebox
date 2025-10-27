package com.bingebox.controller;

import com.bingebox.config.TokenService;
import com.bingebox.controller.request.LoginRequest;
import com.bingebox.controller.request.UserRequest;
import com.bingebox.controller.response.LoginResponse;
import com.bingebox.controller.response.UserResponse;
import com.bingebox.entity.User;
import com.bingebox.exception.UserNameOrPasswordInvalidException;
import com.bingebox.mapper.UserMapper;
import com.bingebox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bingebox/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        User savedUser = userService.save(UserMapper.toUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserResponse(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();

            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(token));
        }
        catch (BadCredentialsException e) {
            throw new UserNameOrPasswordInvalidException("User or password invalid!");
        }

    }

}
