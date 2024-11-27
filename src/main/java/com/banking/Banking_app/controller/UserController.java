package com.banking.Banking_app.controller;

import com.banking.Banking_app.dto.BankResponse;
import com.banking.Banking_app.dto.CreditDebitRequest;
import com.banking.Banking_app.dto.EnquiryRequest;
import com.banking.Banking_app.dto.UserRequest;
import com.banking.Banking_app.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse credit(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }

    @PostMapping("/debit")
    public BankResponse debit(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("/createUser")
    public BankResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
}
