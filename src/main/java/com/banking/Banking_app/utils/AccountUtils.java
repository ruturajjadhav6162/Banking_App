package com.banking.Banking_app.utils;

import java.time.Year;
import java.util.Date;

public class AccountUtils {

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
