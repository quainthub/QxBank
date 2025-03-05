package com.quaint.qx_bank.service;

import com.itextpdf.text.DocumentException;
import com.quaint.qx_bank.dto.BankStatementRequest;
import com.quaint.qx_bank.entity.Transaction;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankStatementService {
    List<Transaction> generateBankStatement(BankStatementRequest bankStatementRequest) throws DocumentException, FileNotFoundException;
}
