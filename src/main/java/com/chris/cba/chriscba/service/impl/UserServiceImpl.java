package com.chris.cba.chriscba.service.impl;

import com.chris.cba.chriscba.dto.*;
import com.chris.cba.chriscba.repository.UserRepository;
import com.tochi.cba.tochicba.dto.*;
import com.chris.cba.chriscba.entity.User;
import com.chris.cba.chriscba.enums.Status;
import com.chris.cba.chriscba.service.TransactionService;
import com.chris.cba.chriscba.service.UserService;
import com.chris.cba.chriscba.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    private final TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status(Status.ACTIVE)
                .build();

        User savedUser = userRepository.save(newUser);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number exists in the database
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditRequest) {
        //checking if Account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(creditRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //getting the user
        User userToCredit = userRepository.findByAccountNumber(creditRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditRequest.getAmount()));
        userRepository.save(userToCredit);
        //saving the transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionAmount(creditRequest.getAmount())
                .transactionType("CREDIT")
                .transactionAccountNumber(userToCredit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(creditRequest.getAccountNumber())
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getOtherName() + " " + userToCredit.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest debitRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(debitRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(debitRequest.getAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(debitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(debitRequest.getAmount()));
        userRepository.save(userToDebit);
        //saving the transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionAmount(debitRequest.getAmount())
                .transactionType("DEBIT")
                .transactionAccountNumber(userToDebit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(debitRequest.getAccountNumber())
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getOtherName() + " " + userToDebit.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        /**
         * get the amount to be transferred
         * check if the account is more than the current account balance
         * check if the account exists
         * check if the account to be transferred to exist
         * debit the account
         * credit the account
         * return success message
         */
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(transferRequest.getAmount()));
        String sourceAccountName = userToDebit.getFirstName() + " " + userToDebit.getOtherName() + " " + userToDebit.getLastName();
        userRepository.save(userToDebit);

        //saving the credit transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionAmount(transferRequest.getAmount())
                .transactionType("DEBIT")
                .transactionAccountNumber(userToDebit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionDto);

        User userToCredit = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(userToCredit);

        //saving the debit transaction
        TransactionDto transactionDto2 = TransactionDto.builder()
                .transactionAmount(transferRequest.getAmount())
                .transactionType("CREDIT")
                .transactionAccountNumber(userToDebit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionDto2);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(transferRequest.getSourceAccountNumber())
                        .accountName(sourceAccountName)
                        .build())
                .build();
    }

}
