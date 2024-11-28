package com.banking.Banking_app.utils;

import java.time.Year;
import java.util.Date;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="This user already has an account created!";
    public static final String ACCOUNT_CREATION_CODE="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Account created Successfully!!";
    public static final String ACCOUNT_NOT_EXISTS_CODE="003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE="This user does not have an account created!";
    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_MESSAGE="User account found!";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE="005";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE="007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE="Account debited Successfully!!";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE="Amount Credited successfully";
//    public static final String ACCOUNT_CREDITED_FAIL_MESSAGE=""
    public static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE="Insufficient Balance";
    public static final String TRANSFER_SUCCESS_CODE="008";
    public static final String TRANSFER_SUCCESS_MESSAGE="Transfer Successfully!!";


    public static String generateAccountNumber() {
        // Account Number starting with 2023 + Random six digits
        Year currentyear=Year.now();
        int min=100000;
        int max =999999;
//    Date date=new Date();
        int randomNumber1=(int)Math.floor((Math.random()*(max-min+1)+min));

        String year=String.valueOf(currentyear);
        String randomNumber=String.valueOf(randomNumber1);
        return year+randomNumber;
    }
}
