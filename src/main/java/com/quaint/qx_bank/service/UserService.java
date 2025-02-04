package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.BankResponse;
import com.quaint.qx_bank.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
