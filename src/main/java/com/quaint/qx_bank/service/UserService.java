package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.BankResponse;
import com.quaint.qx_bank.dto.CreditDebitRequest;
import com.quaint.qx_bank.dto.InquiryRequest;
import com.quaint.qx_bank.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceInquiry(InquiryRequest inquiryRequest);
    String nameInquiry(InquiryRequest inquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
}
