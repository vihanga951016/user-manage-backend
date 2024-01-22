package com.manage.userbackend.controller;

import com.manage.userbackend.beans.UserBean;
import com.manage.userbackend.beans.requests.auth.UserLoginRequestBean;
import com.manage.userbackend.services.AuthServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServices authServices;

    @PostMapping("/admin-registration")
    public ResponseEntity register(@RequestBody UserBean userBean, HttpServletRequest request) {
        return authServices.register(userBean, request);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginRequestBean requestBean, HttpServletRequest request) {
        return authServices.userLogin(requestBean, request);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        return authServices.userLogout(request);
    }
}

