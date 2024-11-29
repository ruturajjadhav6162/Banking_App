package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.dto.*;
import com.banking.Banking_app.entity.User;
import com.banking.Banking_app.repository.UserRepository;
import com.banking.Banking_app.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

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
            return "ACCOUNT DOES NOT EXISTS";
        }
        User info=userRepository.findByAccountnumber(request.getAccountNumber());
        return info.getFirstName()+" "+info.getLastName()+" "+info.getOtherName();
    }

    @Override
    @Transactional
    public BankResponse creditAccount(CreditDebitRequest request) {
        //checking if account exists
        if(!userRepository.existsByAccountnumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit=userRepository.findByAccountnumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);
        int accountNo=Integer.parseInt(userToCredit.getAccountnumber())%10000;
        EmailDetails email=EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("MONEY CREDITED TO ACCOUNT")
                .messagebody("Your Account No Ending With "+accountNo+" has been credited with "+request.getAmount()
                        +" INR\n Account Balance : "+userToCredit.getAccountBalance()+" INR")
                .build();
        emailService.sendEmailAlert(email);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToCredit.getAccountnumber())
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public BankResponse debitAccount(CreditDebitRequest request) {
        if(!userRepository.existsByAccountnumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit=userRepository.findByAccountnumber(request.getAccountNumber());
        BigInteger availableBalance=userToDebit.getAccountBalance().toBigInteger();
        BigInteger amountToDebit=request.getAmount().toBigInteger();
        if(amountToDebit.intValue()>availableBalance.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(userToDebit.getAccountnumber())
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }

        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));

        userRepository.save(userToDebit);

        int accountNo=Integer.parseInt(userToDebit.getAccountnumber())%10000;
        EmailDetails email=EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("MONEY DEBITED FROM ACCOUNT")
                .messagebody("Your Account No Ending With "+accountNo+" has been debited with "+request.getAmount()
                        +" INR\n Account Balance : "+userToDebit.getAccountBalance()+" INR")
                .build();
        emailService.sendEmailAlert(email);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToDebit.getAccountnumber())
                        .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public BankResponse transferAmount(TransferRequest request) {
        boolean isSourceAccountExists=userRepository.existsByAccountnumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExists=userRepository.existsByAccountnumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExists||!isSourceAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccountUser=userRepository.findByAccountnumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);

        User destinationAccountUser=userRepository.findByAccountnumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        int accountNo=Integer.parseInt(sourceAccountUser.getAccountnumber())%10000;

        int accountNo2=Integer.parseInt(destinationAccountUser.getAccountnumber())%10000;
        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(sourceAccountUser.getEmail())
                .subject("MONEY DEBITED FROM ACCOUNT")
                .messagebody("Your Account No Ending With "+accountNo+" has been debited with "+request.getAmount()
                        +" INR Towards Account number Ending with "+accountNo2+"\n Account Balance : "+sourceAccountUser.getAccountBalance()+" INR")
                .build();
        emailService.sendEmailAlert(emailDetails);



        EmailDetails email=EmailDetails.builder()
                .recipient(destinationAccountUser.getEmail())
                .subject("MONEY CREDITED TO ACCOUNT")
                .messagebody("Your Account No Ending With "+accountNo2+" has been credited with "+request.getAmount()
                        +" INR   From Account Number Ending with "+accountNo+"\n Account Balance : "+destinationAccountUser.getAccountBalance()+" INR")
                .build();
        emailService.sendEmailAlert(email);
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
