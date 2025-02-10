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

    @Autowired
    TransactionService transactionService;

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

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        //check if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);

        //Save transaction
        TransactionDto creditTransaction = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditDebitRequest.getAmount())
                .build();
        transactionService.saveTransaction(creditTransaction);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("ACCOUNT CREDITED")
                .messageBody("Your new account has been credited $"+creditDebitRequest.getAmount()+"\n" +
                        "Your account details:\n" +
                        "Account Name: "+userToCredit.getAccountName()+"\n" +
                        "Account Number: "+userToCredit.getAccountNumber()+"\n" +
                        "Account Balance: "+userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);

        AccountInfo userAccountInfo = AccountInfo.builder()
                .accountName(userToCredit.getAccountName())
                .accountNumber(userToCredit.getAccountNumber())
                .accountBalance(userToCredit.getAccountBalance())
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(userAccountInfo)
                .build();

    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        //check if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //check if the account has enough balance
        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(creditDebitRequest.getAmount())<0){
            AccountInfo userAccountInfo = AccountInfo.builder()
                    .accountName(userToDebit.getAccountName())
                    .accountNumber(userToDebit.getAccountNumber())
                    .accountBalance(userToDebit.getAccountBalance())
                    .build();
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_FUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_FUND_MESSAGE)
                    .accountInfo(userAccountInfo)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        userRepository.save(userToDebit);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("ACCOUNT DEBITED")
                .messageBody("Your new account has been debited $"+creditDebitRequest.getAmount()+"\n" +
                        "Your account details:\n" +
                        "Account Name: "+userToDebit.getAccountName()+"\n" +
                        "Account Number: "+userToDebit.getAccountNumber()+"\n" +
                        "Account Balance: "+userToDebit.getAccountBalance())
                .build();

        TransactionDto debitTransaction = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(creditDebitRequest.getAmount())
                .build();
        transactionService.saveTransaction(debitTransaction);

        emailService.sendEmailAlert(emailDetails);

        AccountInfo userAccountInfo = AccountInfo.builder()
                .accountName(userToDebit.getAccountName())
                .accountNumber(userToDebit.getAccountNumber())
                .accountBalance(userToDebit.getAccountBalance())
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .accountInfo(userAccountInfo)
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        //get the source account and destination account
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber());
        if (!isSourceAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if (!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //confirm there is enough balance in the source account
        User userToDebit = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(transferRequest.getAmount())<0){
            AccountInfo userAccountInfo = AccountInfo.builder()
                    .accountName(userToDebit.getAccountName())
                    .accountNumber(userToDebit.getAccountNumber())
                    .accountBalance(userToDebit.getAccountBalance())
                    .build();
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_FUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_FUND_MESSAGE)
                    .accountInfo(userAccountInfo)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(transferRequest.getAmount()));
        userToCredit.setAccountBalance(userToDebit.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(userToDebit);
        userRepository.save(userToCredit);

        TransactionDto transferTransaction = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("TRANSFER")
                .amount(transferRequest.getAmount())
                .build();
        transactionService.saveTransaction(transferTransaction);

        //Send email alert to source user
        EmailDetails emailSourceUser = EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("TRANSFER COMPLETED")
                .messageBody("Transfer has been completed for the following accounts\n" +
                        "Destination Account Number: "+userToCredit.getAccountNumber()+"\n" +
                        "Transfer amount: "+transferRequest.getAmount()+"\n" +
                        "Your account details:\n" +
                        "Account Name: "+userToDebit.getAccountName()+"\n" +
                        "Account Number: "+userToDebit.getAccountNumber()+"\n" +
                        "Account Balance: "+userToDebit.getAccountBalance())
                .build();

        //Send email alert to source user
        EmailDetails emailDestinationUser = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("TRANSFER COMPLETED")
                .messageBody("Transfer has been completed for the following accounts\n" +
                        "Source Account Number: "+userToDebit.getAccountNumber()+"\n" +
                        "Transfer amount: "+transferRequest.getAmount()+"\n" +
                        "Your account details:\n" +
                        "Account Name: "+userToCredit.getAccountName()+"\n" +
                        "Account Number: "+userToCredit.getAccountNumber()+"\n" +
                        "Account Balance: "+userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailSourceUser);
        emailService.sendEmailAlert(emailDestinationUser);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FUND_TRANSFERRED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FUND_TRANSFERRED_MESSAGE)
                .accountInfo(null)
                .build();
    }
    // balance inquiry, name inquiry, credit, debit and transfer

}
