package com.quaint.qx_bank.controller;

import com.quaint.qx_bank.dto.*;
import com.quaint.qx_bank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name="User Account Management APIs")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(
            summary = "Create new user account",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Inquire account balance",
            description = "Inquire account balance given an account number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 Success"
    )
    @GetMapping("/balanceInquiry")
    public BankResponse balanceInquiry(@RequestBody InquiryRequest inquiryRequest){
        return userService.balanceInquiry(inquiryRequest);
    }

    @Operation(
            summary = "Inquire account name",
            description = "Inquire account name given an account number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 Success"
    )
    @GetMapping("/nameInquiry")
    public String nameInquiry(@RequestBody InquiryRequest inquiryRequest){
        return userService.nameInquiry(inquiryRequest);
    }

    @Operation(
            summary = "Credit an account",
            description = "Credit an account given an account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created"
    )
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }

    @Operation(
            summary = "Debit an account",
            description = "Debit an account given an account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created"
    )
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }

    @Operation(
            summary = "Transfer an amount",
            description = "Transfer an amount given an source account number and a destination account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created"
    )
    @PostMapping("/transfer")
    public BankResponse debitAccount(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }
}
