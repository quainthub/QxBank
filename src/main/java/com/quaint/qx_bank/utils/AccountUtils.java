package com.quaint.qx_bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCUNT_EXISTS_CODE = "001";

    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created.";

    public static final String ACCOUNT_CREATION_CODE = "002";

    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";

    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";

    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with provided Account Number does not exist";

    public static final String ACCOUNT_FOUND_CODE = "004";

    public static final String ACCOUNT_FOUND_MESSAGE = "User Account found";

    public static String generateAccountNumber(){
        /**
         * 2023 + random 6 digits
         */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generate a random number between min and max
        int randomNumber = (int)(Math.random() *(max-min+1)+min);
        // concat year and randomNumber to a new id
        String year = String.valueOf(currentYear);
        String randomNumberStr = String.valueOf(randomNumber);
        StringBuilder accountNumber = new StringBuilder();

        accountNumber.append(year);
        accountNumber.append(randomNumberStr);
        return accountNumber.toString();
    }
}
