package com.banking.Banking_app.service.impl;

import com.banking.Banking_app.dto.BankResponse;
import com.banking.Banking_app.dto.CreditDebitRequest;
import com.banking.Banking_app.dto.EnquiryRequest;
import com.banking.Banking_app.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
}
