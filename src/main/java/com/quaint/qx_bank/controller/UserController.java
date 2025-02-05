package com.quaint.qx_bank.controller;

import com.quaint.qx_bank.dto.BankResponse;
import com.quaint.qx_bank.dto.InquiryRequest;
import com.quaint.qx_bank.dto.UserRequest;
import com.quaint.qx_bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @GetMapping("/balanceInquiry")
    public BankResponse balanceInquiry(@RequestBody InquiryRequest inquiryRequest){
        return userService.balanceInquiry(inquiryRequest);
    }

    @GetMapping("/nameInquiry")
    public String nameInquiry(@RequestBody InquiryRequest inquiryRequest){
        return userService.nameInquiry(inquiryRequest);
    }
}
