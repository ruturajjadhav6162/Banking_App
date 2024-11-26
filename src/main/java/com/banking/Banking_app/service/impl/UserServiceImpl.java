package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.dto.AccountInfo;
import com.banking.Banking_app.dto.BankResponse;
import com.banking.Banking_app.dto.UserRequest;
import com.banking.Banking_app.entity.User;
import com.banking.Banking_app.repository.UserRepository;
import com.banking.Banking_app.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
//        Creating an Account - Saving info in db
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            User user=userRepository.findByEmail(userRequest.getEmail());
            AccountInfo accountInfo= AccountInfo.builder()
                    .accountNumber(user.getAccountnumber())
                    .accountName(user.getFirstName()+" "+user.getLastName()+" "+user.getOtherName())
                    .accountBalance(user.getAccountBalance())
                    .build();
            return BankResponse.builder()
                    .accountInfo(accountInfo)
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .build();
        }
        User newUser=User.builder()
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
        User savedUser=userRepository.save(newUser);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountnumber())
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }
}
