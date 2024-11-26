package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.dto.AccountInfo;
import com.banking.Banking_app.dto.BankResponse;
import com.banking.Banking_app.dto.UserRequest;
import com.banking.Banking_app.entity.User;
import com.banking.Banking_app.utils.AccountUtils;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
//        Creating an Account - Saving info in db
        User user=User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .otherName(userRequest.getOtherName())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountnumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhonneNumber(userRequest.getAlternativePhonneNumber())
                .status("ACTIVE")
                .build();
    }
}
