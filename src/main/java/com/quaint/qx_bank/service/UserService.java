package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.*;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceInquiry(InquiryRequest inquiryRequest);
    String nameInquiry(InquiryRequest inquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transfer(TransferRequest transferRequest);
}
