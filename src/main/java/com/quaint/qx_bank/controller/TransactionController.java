package com.quaint.qx_bank.controller;

import java.io.FileNotFoundException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.quaint.qx_bank.dto.BankStatementRequest;
import com.quaint.qx_bank.entity.Transaction;
import com.quaint.qx_bank.service.BankStatementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@Tag(name="Transaction APIs")
@AllArgsConstructor
public class TransactionController {
    private BankStatementService bankStatementService;

    @GetMapping("/bankStatement")
    public List<Transaction> generateBankStatement(@RequestBody BankStatementRequest bankStatementRequest) throws DocumentException, FileNotFoundException {
        return bankStatementService.generateBankStatement(bankStatementRequest);
    }
}
