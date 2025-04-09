package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.TransactionInfo;
import com.quaint.qx_bank.entity.Transaction;
import com.quaint.qx_bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionInfo transactionInfo) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionInfo.getTransactionType())
                .amount(transactionInfo.getAmount())
                .accountNumber(transactionInfo.getAccountNumber())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully!");
    }
}
