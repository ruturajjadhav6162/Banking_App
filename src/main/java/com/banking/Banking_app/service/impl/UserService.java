package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.dto.BankResponse;
import com.banking.Banking_app.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
