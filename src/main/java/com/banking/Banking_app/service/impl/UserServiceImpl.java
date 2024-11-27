package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.dto.*;
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
    @Autowired
    EmailServiceImpl emailService;

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
                .gender(userRequest.getGender())
                .accountnumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhonneNumber(userRequest.getAlternativePhonneNumber())
                .status("ACTIVE")
                .build();
        User savedUser=userRepository.save(newUser);

        EmailDetails emailDetails=EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATED")
                .messagebody("CRONGRATULATION'S YOUR ACCOUNT HAS BEEN CREATED SUCCESSFULLY\n Your Account Details\n Account Number : "+savedUser.getAccountnumber()
                +"\nAccount Name : "+savedUser.getFirstName()+" "+savedUser.getLastName())
                .build();

        emailService.sendEmailAlert(emailDetails);

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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // Check if account exists in database
        if(!userRepository.existsByAccountnumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User info=userRepository.findByAccountnumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(info.getAccountnumber())
                        .accountName(info.getFirstName()+" "+info.getLastName()+" "+info.getOtherName())
                        .accountBalance(info.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        if(!userRepository.existsByAccountnumber(request.getAccountNumber())) {
            return AccountUtils.ACCOUNT_NOT_EXISTS_CODE;
        }
        User info=userRepository.findByAccountnumber(request.getAccountNumber());
        return info.getFirstName()+" "+info.getLastName()+" "+info.getOtherName();
    }
}
