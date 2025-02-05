package com.quaint.qx_bank.service;

import com.quaint.qx_bank.dto.*;
import com.quaint.qx_bank.entity.User;
import com.quaint.qx_bank.repository.UserRepository;
import com.quaint.qx_bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Create an account - saving a new user into the db
         * check if user already has an account
         */
        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountName()
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser = userRepository.save(newUser);
        AccountInfo savedUserAccountInfo = AccountInfo.builder()
                .accountName(savedUser.getAccountName())
                .accountNumber(savedUser.getAccountNumber())
                .accountBalance(savedUser.getAccountBalance())
                .build();

        //Send email alert to user
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Your new account has been created!\n" +
                        "Your account details:\n" +
                        "Account Name: "+savedUserAccountInfo.getAccountName()+"\n" +
                        "Account Number: "+savedUserAccountInfo.getAccountNumber()+"\n" +
                        "Account Balance: "+savedUserAccountInfo.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(savedUserAccountInfo)
                .build();
    }

    @Override
    public BankResponse balanceInquiry(InquiryRequest inquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(inquiryRequest.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(inquiryRequest.getAccountNumber());
        AccountInfo foundUserAccountInfo = AccountInfo.builder()
                .accountName(foundUser.getAccountName())
                .accountNumber(foundUser.getAccountNumber())
                .accountBalance(foundUser.getAccountBalance())
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(foundUserAccountInfo)
                .build();
    }

    @Override
    public String nameInquiry(InquiryRequest inquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(inquiryRequest.getAccountNumber());
        if (!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(inquiryRequest.getAccountNumber());
        return foundUser.getAccountName();
    }
    // balance inquiry, name inquiry, credit, debit and transfer

}
