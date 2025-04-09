package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.TransactionInfo;

public interface TransactionService {
    void saveTransaction(TransactionInfo transactionInfo);
}
