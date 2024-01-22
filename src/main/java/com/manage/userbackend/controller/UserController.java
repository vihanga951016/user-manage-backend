package com.manage.userbackend.controller;

import com.manage.userbackend.beans.UserBean;
import com.manage.userbackend.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    @GetMapping("/get-all-users")
    public ResponseEntity getAllUsers(HttpServletRequest request) {
        return userServices.getAllUsers(request);
    }

    @PostMapping("/add-user")
    public ResponseEntity addUser(@RequestBody UserBean userBean,
                                  HttpServletRequest request) {
        return userServices.addUser(userBean, request);
    }

    @PostMapping("/update-user")
    public ResponseEntity updateUser(@RequestBody UserBean userBean,
                                  HttpServletRequest request) {
        return userServices.updateUser(userBean, request);
    }

    @PostMapping("/update-activation/user/{uid}")
    public ResponseEntity userActivation(@PathVariable Integer uid,
                                     HttpServletRequest request) {
        return userServices.userAvailability(uid, request);
    }

    @PostMapping("/delete-user/user/{uid}")
    public ResponseEntity deleteUser(@PathVariable Integer uid,
                                         HttpServletRequest request) {
        return userServices.deleteUser(uid, request);
    }
}
