package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
