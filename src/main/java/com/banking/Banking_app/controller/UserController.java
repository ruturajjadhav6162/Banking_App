package com.banking.Banking_app.controller;

import com.banking.Banking_app.dto.BankResponse;
import com.banking.Banking_app.dto.UserRequest;
import com.banking.Banking_app.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/createUser")
    public BankResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
}
